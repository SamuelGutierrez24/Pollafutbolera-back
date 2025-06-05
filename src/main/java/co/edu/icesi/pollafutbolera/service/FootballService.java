// Java
package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.mapper.MatchMapper;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FootballService {

    private final FootballApiClient footballApiClient;
    private final MatchService matchService;
    private final MatchMapper matchMapper;
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;
    private final StageService stageService;

    public ResponseEntity<List<MatchDTO>> fetchAndSaveFixtures(Integer league, Integer season, String round) {
        // Llamada a la API con los parámetros proporcionados
        Mono<List<MatchDTO>> mono = footballApiClient.getFixtures(league, season, round)
                .flatMap(jsonNode -> {
                    // Mapeo directo de cada fixture del JsonNode a MatchDTO
                    List<MatchDTO> matchDTOs = extractMatchesFromJson(jsonNode);

                    // Guardar los partidos de forma reactiva
                    matchDTOs.forEach(matchService::createMatch);
                    return Mono.just(matchDTOs);
                });
        // Esperar a que se complete la llamada y devolver la respuesta
        return mono.blockOptional(Duration.ofSeconds(30))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(500).build());

    }

    public ResponseEntity<List<MatchDTO>> saveAllFixtures(Integer league, Integer season) {
        // Hacer la llamada a la API con los parámetros proporcionados
        Mono<List<MatchDTO>> mono = footballApiClient.getAllFixtures(league, season)
                .flatMap(jsonNode -> {
                    // Mapeo directo de cada fixture del JsonNode a MatchDTO
                    List<MatchDTO> matchDTOs = extractMatchesFromJson(jsonNode);

                    // Guardar los partidos en la base de datos
                    matchDTOs.forEach(matchService::createMatch);

                    return Mono.just(matchDTOs); // Devolver la lista de DTOs
                });
        // Esperar a que se complete la llamada y devolver la respuesta
        return mono.blockOptional()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(500).build());
    }

    public void saveMatchById(Integer id) {
        // Llamada a la API para obtener un partido específico
        Mono<MatchDTO> mono = footballApiClient.getMatch(id)
                .flatMap(jsonNode -> {
                    // Mapeo directo del JsonNode a MatchDTO
                    List<MatchDTO> matchDTOs = extractMatchesFromJson(jsonNode);
                    matchDTOs.forEach(matchService:: apiMatchSave);

                    return Mono.just(matchDTOs.get(0));
                });
        // Esperar a que se complete la llamada y devolver la respuesta
        mono.blockOptional(Duration.ofSeconds(30))
                .map(ResponseEntity::ok);
        ResponseEntity.status(500).build();
    }



    // Mapeo directo del JsonNode a MatchDTO
    private List<MatchDTO> extractMatchesFromJson(JsonNode jsonNode) {
        // Extraer los fixtures del JsonNode
        ArrayNode fixturesNode = (ArrayNode) jsonNode.get("response");
        if (fixturesNode == null || fixturesNode.isEmpty()) {
            throw new RuntimeException("No fixtures found in the response");
        }

        // Mapear cada fixture a MatchDTO usando el MatchMapper
        List<MatchDTO> matchDTOs = new ArrayList<>();
        for (JsonNode fixtureNode : fixturesNode) {
            MatchDTO matchDTO = matchMapper.fromJson(fixtureNode);  // Usar el método fromJson de MatchMapper
            matchDTOs.add(matchDTO);
        }

        return matchDTOs;
    }


}