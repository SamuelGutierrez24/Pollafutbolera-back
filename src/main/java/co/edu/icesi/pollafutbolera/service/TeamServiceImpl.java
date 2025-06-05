package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import co.edu.icesi.pollafutbolera.exception.TeamNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.TeamMapper;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository repository;
    private final TeamMapper mapper;
    private final MatchRepository matchRepo;
    private final FootballApiClient footballApiClient;


    @Override
    public ResponseEntity<List<TeamDTO>> findAll() {
        List<Team> teams = repository.findAll();
        List<TeamDTO> teamDTOs = mapper.listToTeamDTO(teams);
        return ResponseEntity.ok(teamDTOs);
    }

    @Override
    public ResponseEntity<TeamDTO> findById(Long id) throws Exception {
        Optional<Team> team = repository.findById(id);
        return team.map(value -> ResponseEntity.ok(mapper.toTeamDTO(value)))
                .orElseThrow(TeamNotFoundException::new);
    }


    @Override
    public ResponseEntity<TeamDTO> createTeam(TeamDTO teamDTO) {
        Team team = mapper.toTeam(teamDTO);
        team = repository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTeamDTO(team));
    }

    @Override
    public ResponseEntity<TeamDTO> save(Long id, TeamDTO teamDTO) throws Exception {
        Optional<Team> existingTeam = repository.findById(id);
        if (existingTeam.isPresent()) {
            Team team = existingTeam.get();
            mapper.updateFromDTO(teamDTO, team);
            repository.save(team);
            return ResponseEntity.ok(mapper.toTeamDTO(team));
        } else {
            throw new TeamNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new TeamNotFoundException();
        }
    }

    public ResponseEntity<Void> fetchAndSaveTeams(Integer league, Integer season) {

        footballApiClient.getTeams(league, season)
                .blockOptional()                               
                .ifPresent(json -> {

                    var response = json.get("response");
                    if (response == null || !response.isArray()) return;

                    response.forEach(wrapper -> {
                        var teamNode = wrapper.get("team");
                        if (teamNode == null) return;

                        Long   id   = teamNode.get("id").asLong();
                        String name = teamNode.get("name").asText("Unknown");
                        String logo = teamNode.get("logo").asText(null);

                        TeamDTO dto = TeamDTO.builder()
                                .id(id)
                                .name(name)
                                .logoUrl(logo)
                                .build();

                        repository.findById(id).ifPresentOrElse(
                                existing -> {                        
                                    mapper.updateFromDTO(dto, existing);
                                    repository.save(existing);
                                },
                                () -> {                              
                                    Team newTeam = mapper.toTeam(dto);   
                                    newTeam.setId(id);                   
                                    repository.save(newTeam);
                                });
                    });
                });

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TeamDTO>> fetchTeamsByLeagueAndSeason(Integer league, Integer season) {
        List<TeamDTO> dtos = footballApiClient.getTeams(league, season)
                .blockOptional()
                .map(json -> {
                    var arr = json.get("response");
                    if (arr == null || !arr.isArray()) {
                        return List.<TeamDTO>of();
                    }
                    return StreamSupport.stream(arr.spliterator(), false)
                            .map(wrapper -> {
                                var node = wrapper.get("team");
                                return TeamDTO.builder()
                                        .id(node.get("id").asLong())
                                        .name(node.get("name").asText())
                                        .logoUrl(node.get("logo").asText(null))
                                        .build();
                            })
                            .toList();
                })
                .orElseGet(List::of);

        return ResponseEntity.ok(dtos);
    }
    @Override
    public  ResponseEntity<List<TeamDTO>> getTeamsInTournament(Long tournamentId) {
        
        List<Match> matches = matchRepo.findByTournamentId(tournamentId);

        if (matches.isEmpty()) {
            
            return ResponseEntity.noContent().build();
        }

        
        Set<Team> uniqueTeams = matches.stream()
                .flatMap(m -> Stream.of(m.getHomeTeam(), m.getAwayTeam()))
                .collect(Collectors.toCollection(LinkedHashSet::new)); 

        List<TeamDTO> teamDTOs = uniqueTeams.stream()
                .map(team -> new TeamDTO(
                        team.getId(),
                        team.getName(),
                        team.getLogoUrl()
                ))
                .sorted(Comparator.comparing(TeamDTO::name))
                .toList();

        
        return ResponseEntity.ok(teamDTOs);

    }
}
