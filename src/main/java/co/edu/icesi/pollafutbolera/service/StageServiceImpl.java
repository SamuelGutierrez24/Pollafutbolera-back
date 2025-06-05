package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.exception.StageNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.StageMapper;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

    private final StageRepository repository;
    private final FootballApiClient footballApiClient;
    private final StageMapper mapper;
    private final MatchRepository matchRepository;

    @Override
    public ResponseEntity<List<StageDTO>> findAll() {
        List<Stage> stages = repository.findAll();
        List<StageDTO> stageDTOs = mapper.listToStageDTO(stages);
        return ResponseEntity.ok(stageDTOs);
    }

    @Override
    public ResponseEntity<StageDTO> findById(Long id) throws Exception {
        Optional<Stage> stage = repository.findById(id);
        return stage.map(value -> ResponseEntity.ok(mapper.toStageDTO(value)))
                .orElseThrow(StageNotFoundException::new);
    }

    @Override
    public ResponseEntity<StageDTO> createStage(StageDTO stageDTO) {
        Stage stage = mapper.toStage(stageDTO);
        stage = repository.save(stage);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toStageDTO(stage));
    }

    @Override
    public ResponseEntity<StageDTO> save(Long id, StageDTO stageDTO) throws Exception {
        Optional<Stage> existingStage = repository.findById(id);
        if (existingStage.isPresent()) {
            Stage stage = existingStage.get();
            mapper.updateFromDTO(stageDTO, stage);
            repository.save(stage);
            return ResponseEntity.ok(mapper.toStageDTO(stage));
        } else {
            throw new StageNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new StageNotFoundException();
        }
    }

    @Override
    public ResponseEntity<List<StageDTO>> getAllStagesApi(Long league, int season) {
        // Hacer la llamada a la API con los parámetros proporcionados
        Mono<List<StageDTO>> mono = footballApiClient.getStagesOfTournament(league, season)
                .flatMap(jsonNode -> {
                    // Mapeo directo de cada fixture del JsonNode a MatchDTO
                    List<StageDTO> stageDTOs = extractMatchesFromJson(jsonNode, league);

                    for (StageDTO stageDTO : stageDTOs) {
                        createStage(stageDTO);
                    }

                    return Mono.just(stageDTOs); // Devolver la lista de DTOs
                });
        return mono.blockOptional()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(500).build());
    }


    private List<StageDTO> extractMatchesFromJson(JsonNode jsonNode, Long leagueId) {
        // Extraer los stages del torneo del JsonNode
        System.out.println(jsonNode);
        ArrayNode fixturesStageNode = (ArrayNode) jsonNode.get("response");

        if (fixturesStageNode == null || fixturesStageNode.isEmpty()) {
            throw new RuntimeException("No fixtures found in the response");
        }

        List<StageDTO> stageDTOs = new ArrayList<>();
        for (JsonNode fixtureStageNode : fixturesStageNode) {
            System.out.println(fixtureStageNode);
            StageDTO stageDTO = mapper.fromJson(fixtureStageNode, leagueId);
            System.out.println(stageDTO);
            stageDTOs.add(stageDTO);
        }

        return stageDTOs;
    }
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<StageDTO>> getStagesByTournament(Long tournamentId) {
        List<Stage> stages = repository.findByTournamentId(tournamentId);
        List<StageDTO> dtos = mapper.listToStageDTO(stages);
        return ResponseEntity.ok(dtos);
    }


    @Override
    public String getCurrentStageName(Long tournamentId) {
        LocalDateTime now = LocalDateTime.now();

        return repository.findByTournamentId(tournamentId).stream()
                .map(stage -> Map.entry(
                        stage,
                        matchRepository
                                .findFirstByTournamentIdAndStageIdAndDateAfterOrderByDateAsc(tournamentId, stage.getId(), now)
                                .map(Match::getDate)
                ))
                .filter(e -> e.getValue().isPresent())
                .min(Comparator.comparing(e -> e.getValue().get()))
                .map(e -> e.getKey().getName())
                .orElse("Sin etapa actual");
    }
}
