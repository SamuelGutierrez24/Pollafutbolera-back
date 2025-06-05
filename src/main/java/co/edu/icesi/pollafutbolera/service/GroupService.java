package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.GroupDTO;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GroupService {

    Mono<List<GroupDTO>> retrieveStandingsAsDTOs(Integer league, Integer season);

    Mono<Void> saveGroupsInDatabase(List<GroupDTO> groupDTOs);

    Mono<Void> retrieveAndSaveStandings(Integer league, Integer season);

    Mono<JsonNode> retrieveGroupStageFixtures(Integer league, Integer season);

    Mono<Void> retrieveAndSaveGroupStageMatches(Integer league, Integer season);

    void linkMatchesToGroups();

    List<GroupDTO> findAllGroupsWithDetails();

    Mono<Void> updateGroupStage(Integer league, Integer season);

    List<GroupDTO> findGroupsByPolla(Long pollaId);

}

