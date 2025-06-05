package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.GroupDTO;
import co.edu.icesi.pollafutbolera.dto.GroupTeamDTO;
import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.model.Group;
import co.edu.icesi.pollafutbolera.model.GroupTeam;
import co.edu.icesi.pollafutbolera.model.Match;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public abstract class GroupMapper {

    @Autowired(required = false)
    private MatchMapper matchMapper;

    @Named("fromStandingsJson")
    public List<GroupDTO> fromStandingsJson(JsonNode jsonNode) {
        List<GroupDTO> groupDTOList = new ArrayList<>();

        JsonNode responseArray = jsonNode.get("response");
        if (responseArray == null || !responseArray.isArray() || responseArray.isEmpty()) {
            return groupDTOList;
        }

        JsonNode leagueNode = responseArray.get(0).get("league");
        if (leagueNode == null) {
            return groupDTOList;
        }

        Long tournamentId = leagueNode.get("id").asLong();
        JsonNode standingsArray = leagueNode.get("standings");
        if (standingsArray == null || !standingsArray.isArray()) {
            return groupDTOList;
        }

        for (JsonNode groupArray : standingsArray) {
            if (!groupArray.isArray() || groupArray.isEmpty()) {
                continue;
            }

            String groupName = groupArray.get(0).get("group").asText("");

            List<GroupTeamDTO> groupTeamDTOs = new ArrayList<>();

            Long firstWinnerTeamId   = null;
            Long secondWinnerTeamId  = null;

            for (JsonNode teamWrapper : groupArray) {
                JsonNode teamNode = teamWrapper.get("team");
                if (teamNode == null) continue;

                Long teamIdFromApi = teamNode.get("id").asLong();
                Integer rank = teamWrapper.get("rank").asInt(0);
                Integer points = teamWrapper.get("points").asInt(0);

                String teamName = teamNode.get("name").asText("PendingName");
                String teamLogo = teamNode.get("logo").asText("PendingLogo");

                
                if (rank == 1) {
                    firstWinnerTeamId = teamIdFromApi;
                } else if (rank == 2) {
                    secondWinnerTeamId = teamIdFromApi;
                }

                
                GroupTeamDTO gtd = GroupTeamDTO.builder()
                        .teamId(teamIdFromApi)
                        .rank(rank)
                        .points(points)
                        .teamName(teamName)
                        .teamLogoUrl(teamLogo)
                        .build();
                groupTeamDTOs.add(gtd);
            }

            
            GroupDTO groupDTO = GroupDTO.builder()
                    .groupName(groupName)
                    .tournamentId(tournamentId)
                    .teams(groupTeamDTOs)
                    .matches(null)  
                    .firstWinnerTeamId(firstWinnerTeamId)
                    .secondWinnerTeamId(secondWinnerTeamId)
                    .build();

            groupDTOList.add(groupDTO);
        }

        return groupDTOList;
    }

    @Mapping(source = "id",          target = "id")
    @Mapping(source = "groupName",   target = "groupName")
    @Mapping(source = "tournamentId",target = "tournamentId")
    
    @Mapping(target = "teams",   expression = "java(mapGroupTeams(entity.getGroupTeams()))")
    
    @Mapping(target = "matches", expression = "java(mapMatches(entity.getMatches()))")
    public abstract GroupDTO toGroupDTO(Group entity);

    @InheritInverseConfiguration
    public abstract Group toGroup(GroupDTO dto);

    protected List<GroupTeamDTO> mapGroupTeams(Set<GroupTeam> groupTeams) {
        if (groupTeams == null) return null;
        return groupTeams.stream()
                .map(gt -> GroupTeamDTO.builder()
                        .teamId(gt.getTeam() != null ? gt.getTeam().getId() : null)
                        .rank(gt.getRank())
                        .points(gt.getPoints())
                        .teamName(gt.getTeam() != null ? gt.getTeam().getName() : null)
                        .teamLogoUrl(gt.getTeam() != null ? gt.getTeam().getLogoUrl() : null)
                        .build())
                .collect(Collectors.toList());
    }

    protected List<MatchDTO> mapMatches(Set<Match> matches) {
        if (matches == null || matchMapper == null) return null;
        return matches.stream()
                .map(matchMapper::toMatchDTO)
                .collect(Collectors.toList());
    }

    @Mapping(source = "id.teamId",  target = "teamId")
    @Mapping(source = "rank",      target = "rank")
    @Mapping(source = "points",    target = "points")
    public abstract GroupTeamDTO toGroupTeamDTO(GroupTeam entity);

    @InheritInverseConfiguration
    public abstract GroupTeam toGroupTeam(GroupTeamDTO dto);

}
