package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.dto.GroupDTO;
import co.edu.icesi.pollafutbolera.dto.GroupTeamDTO;
import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.mapper.GroupMapper;
import co.edu.icesi.pollafutbolera.mapper.MatchMapper;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final FootballApiClient footballApiClient;

    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;
    private final GroupTeamRepository groupTeamRepository;
    private final GroupMatchRepository groupMatchRepository;
    private final MatchRepository matchRepository;
    private final StageRepository stageRepository;
    private final PollaRepository pollaRepository;

    private final GroupMapper groupMapper;
    private final MatchMapper matchMapper;
    private final MatchService matchService;

    @Override
    public Mono<List<GroupDTO>> retrieveStandingsAsDTOs(Integer league, Integer season) {
        return footballApiClient.getStandings(league, season)
                .map(jsonNode -> groupMapper.fromStandingsJson(jsonNode));
    }

    @Override
    public Mono<Void> saveGroupsInDatabase(List<GroupDTO> groupDTOs) {
        return Mono.fromRunnable(() -> {
            
            for (GroupDTO gDTO : groupDTOs) {

                List<Group> existing = groupRepository
                        .findByGroupNameAndTournamentId(gDTO.groupName(), gDTO.tournamentId())
                        .orElse(Collections.emptyList());

                if (!existing.isEmpty()) {
                    throw new IllegalStateException("A group named '" + gDTO.groupName()
                            + "' already exists in tournament " + gDTO.tournamentId());
                }


                Group groupEntity = Group.builder()
                        .groupName(gDTO.groupName())
                        .tournamentId(gDTO.tournamentId())
                        .firstWinnerTeamId(gDTO.firstWinnerTeamId())
                        .secondWinnerTeamId(gDTO.secondWinnerTeamId())
                        .build();
                Group savedGroup = groupRepository.save(groupEntity);

                
                for (GroupTeamDTO gtDTO : gDTO.teams()) {
                    Long apiTeamId = gtDTO.teamId();   

                    
                    Team team = ensureTeamExists(
                            apiTeamId,
                            gtDTO.teamName(),       
                            gtDTO.teamLogoUrl()     
                    );
                    GroupTeam groupTeam = GroupTeam.builder()
                            .id(new GroupTeamId(savedGroup.getId(), team.getId()))
                            .groupStage(savedGroup)
                            .team(team)
                            .rank(gtDTO.rank())
                            .points(gtDTO.points())
                            .build();
                    groupTeamRepository.save(groupTeam);
                }
            }
        });
    }

    private Team ensureTeamExists(Long teamId, String teamName, String logoUrl) {
        Optional<Team> optTeam = teamRepository.findById(teamId);
        if (optTeam.isPresent()) {
            return optTeam.get();
        }
        
        Team newTeam = Team.builder()
                .id(teamId)          
                .name(teamName)      
                .logoUrl(logoUrl)    
                .build();
        return teamRepository.save(newTeam);
    }

    @Override
    public Mono<Void> retrieveAndSaveStandings(Integer league, Integer season) {
        return retrieveStandingsAsDTOs(league, season)
                .flatMap(this::saveGroupsInDatabase);
    }

    @Override
    public Mono<JsonNode> retrieveGroupStageFixtures(Integer league, Integer season) {
        // Usamos league como tournamentId
        Long tournamentId = league.longValue();
        return fetchGroupStageNames(tournamentId)
                .flatMap(stageName -> footballApiClient.getFixtures(league, season, stageName))
                .collectList()
                .map(list -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayNode result = mapper.createArrayNode();
                    list.forEach(result::add);
                    return (JsonNode) result;
                });
    }
    
    @Override
    public Mono<Void> retrieveAndSaveGroupStageMatches(Integer league, Integer season) {
        Long tournamentId = league.longValue();
        return fetchGroupStageNames(tournamentId)
                .flatMap(stageName -> footballApiClient.getFixtures(league, season, stageName)
                        .flatMapMany(json -> {
                            ArrayNode arr = (ArrayNode) json.get("response");
                            if (arr == null || arr.isEmpty()) return Flux.empty();
                            return Flux.fromIterable(arr).map(matchMapper::fromJson);
                        })
                        .flatMap(dto -> Mono.fromCallable(() -> matchService.createMatch(dto).getBody()))
                )
                .flatMap(saved -> Mono.fromRunnable(() -> associateMatchToGroup(saved)))
                .then();
    }

    private void associateMatchToGroup(MatchDTO savedMatch) {
        Stage stageEntity = stageRepository.findById(savedMatch.stageId()).orElse(null);
        if (stageEntity == null) return;

        String groupName = stageEntity.getName();
        Long tournamentId = savedMatch.tournamentId();

        List<Group> groups = groupRepository.findByGroupNameAndTournamentId(groupName, tournamentId)
                .orElse(Collections.emptyList());
        if (groups.isEmpty()) return;

        Group groupStage = groups.get(0);
        Match matchEntity = matchRepository.findById(savedMatch.id()).orElse(null);
        if (matchEntity == null) return;

        GroupMatch groupMatch = GroupMatch.builder()
                .id(new GroupMatchId(groupStage.getId(), matchEntity.getId()))
                .groupStage(groupStage)
                .match(matchEntity)
                .build();
        groupMatchRepository.save(groupMatch);
    }

    @Override
    public void linkMatchesToGroups() {
        List<Match> allMatches = matchRepository.findAll();
        for (Match match : allMatches) {
            if (match.getHomeTeam() == null) continue;

            
            List<GroupTeam> groupTeams = groupTeamRepository.findByTeamId(match.getHomeTeam().getId());
            for (GroupTeam groupTeam : groupTeams) {
                Group groupStage = groupTeam.getGroupStage();
                if (groupStage == null) continue;

                GroupMatch groupMatch = GroupMatch.builder()
                        .id(new GroupMatchId(groupStage.getId(), match.getId()))
                        .groupStage(groupStage)
                        .match(match)
                        .build();
                groupMatchRepository.save(groupMatch);
            }
        }
    }

    @Override
    public Mono<Void> updateGroupStage(Integer league, Integer season) {
        Long tournamentId = league.longValue();
        return Mono.fromRunnable(() -> {
            updateMatchesForGroupStage(league, season, tournamentId);
            updateGroupsRankAndPoints(league, season);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> findAllGroupsWithDetails() {
        List<Group> groups = groupRepository.findAllWithDetails();

        groups.sort(Comparator.comparing(Group::getGroupName));

        return groups.stream()
                .map(group -> {

                    List<GroupTeam> sortedTeams = group.getGroupTeams().stream()
                            .sorted(Comparator.comparing(GroupTeam::getRank))
                            .collect(Collectors.toList());

                    group.setGroupTeams(new LinkedHashSet<>(sortedTeams));

                    return groupMapper.toGroupDTO(group);
                })
                .collect(Collectors.toList());
    }


    private void updateMatchesForGroupStage(Integer league, Integer season, Long tournamentId) {
        fetchGroupStageNames(tournamentId)
                .flatMap(stageName -> footballApiClient.getFixtures(league, season, stageName)
                        .flatMapMany(json -> {
                            ArrayNode arr = (ArrayNode) json.get("response");
                            if (arr == null || arr.isEmpty()) return Flux.empty();
                            return Flux.fromIterable(arr).map(matchMapper::fromJson);
                        })
                        .flatMap(this::upsertMatch)
                )
                .blockLast();
    }

    private Mono<MatchDTO> upsertMatch(MatchDTO matchDTO) {
        return Mono.fromCallable(() -> {
            Optional<Match> opt = matchRepository.findById(matchDTO.id());
            if (opt.isPresent()) {
                
                Match existing = opt.get();
                matchMapper.updateFromDTO(matchDTO, existing);
                
                matchRepository.save(existing);
            } else {
                
                Match newMatch = matchMapper.toMatch(matchDTO);
                matchRepository.save(newMatch);
            }
            return matchDTO;
        });
    }

    private void updateGroupsRankAndPoints(Integer league, Integer season) {
        
        JsonNode standings = footballApiClient.getStandings(league, season).block();
        
        List<GroupDTO> groupDTOs = groupMapper.fromStandingsJson(standings);

        
        for (GroupDTO gDTO : groupDTOs) {
            
            List<Group> existingGroups = groupRepository
                    .findByGroupNameAndTournamentId(gDTO.groupName(), gDTO.tournamentId())
                    .orElse(Collections.emptyList());
            if (existingGroups.isEmpty()) {
                
                continue;
            }

            
            Group group = existingGroups.get(0);

            
            for (GroupTeamDTO gtDTO : gDTO.teams()) {
                GroupTeamId gtId = new GroupTeamId(group.getId(), gtDTO.teamId());
                Optional<GroupTeam> optGT = groupTeamRepository.findById(gtId);
                if (optGT.isPresent()) {
                    GroupTeam groupTeam = optGT.get();
                    groupTeam.setRank(gtDTO.rank());
                    groupTeam.setPoints(gtDTO.points());
                    groupTeamRepository.save(groupTeam);
                } else {
                    
                    Team team = ensureTeamExists(gtDTO.teamId(), gtDTO.teamName(), gtDTO.teamLogoUrl());
                    GroupTeam newGT = GroupTeam.builder()
                            .id(gtId)
                            .groupStage(group)
                            .team(team)
                            .rank(gtDTO.rank())
                            .points(gtDTO.points())
                            .build();
                    groupTeamRepository.save(newGT);
                }
            }

            
            group.setFirstWinnerTeamId(gDTO.firstWinnerTeamId());
            group.setSecondWinnerTeamId(gDTO.secondWinnerTeamId());
            groupRepository.save(group);
        }
    }

    private Flux<String> fetchGroupStageNames(Long tournamentId) {
        return Mono.fromCallable(() ->
                        stageRepository.findByTournament_IdAndNameContainingIgnoreCase(
                                tournamentId, "group"))
                .flatMapMany(Flux::fromIterable)
                .map(Stage::getName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> findGroupsByPolla(Long pollaId) {

        Long tournamentId = pollaRepository.findById(pollaId)
                .orElseThrow(() -> new IllegalArgumentException("Polla not found"))
                .getTournament()
                .getId();

        List<Group> groups = groupRepository
                .findAllWithDetailsByTournamentId(tournamentId);

        groups.sort(Comparator.comparing(Group::getGroupName));

        return groups.stream()
                .peek(g -> {
                    List<GroupTeam> sorted = g.getGroupTeams().stream()
                            .sorted(Comparator.comparing(GroupTeam::getRank))
                            .toList();
                    g.setGroupTeams(new LinkedHashSet<>(sorted));
                })
                .map(groupMapper::toGroupDTO)
                .toList();
    }

}
