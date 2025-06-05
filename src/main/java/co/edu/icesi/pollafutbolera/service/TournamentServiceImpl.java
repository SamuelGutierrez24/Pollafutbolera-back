package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.ApiFootballClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.exception.TournamentNotFoundException;
import co.edu.icesi.pollafutbolera.exception.TournamentStatisticsNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentStatisticsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentStatisticsRepository tournamentStatisticsRepository;
    private final TournamentMapper tournamentMapper;
    private final ApiFootballClient apiFootballClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EntityManager entityManager;
    private final FootballApiClient footballApiClient;
    private final StageService stageService;
    @Override
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        return ResponseEntity.ok(tournaments.stream()
                .map(tournamentMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<TournamentStatistics> getTournamentStatistics(Long tournamentId) {
        TournamentStatistics tournamentStatistics = tournamentStatisticsRepository
                .findById(tournamentId)
                .orElseThrow(() -> new TournamentStatisticsNotFoundException("No se encontraron estadísticas para el torneo con ID: " + tournamentId));

        return ResponseEntity.ok(tournamentStatistics);
    }

    @Override
    public ResponseEntity<TournamentDTO> getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .map(tournament -> ResponseEntity.ok(tournamentMapper.toDTO(tournament)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // NO SE RECOMIENDA USAR, USAR SOLO EN CASOS ESPECIALES
    // PARA CREAR UN NUEVO TORNEO, USAR EL REGISTRO POR API
    // RIESGO DE CREAR UN TORNEO CON ID CRUZADA
    @Override
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
        Tournament savedTournament = tournamentRepository.save(tournament);
        return ResponseEntity.ok(tournamentMapper.toDTO(savedTournament));
    }

    @Override
    public ResponseEntity<Tournament> getTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Torneo no encontrado con ID: " + tournamentId));

        return ResponseEntity.ok(tournament);
    }

    @Override
    public ResponseEntity<TournamentDTO> updateTournament(Long id, TournamentDTO tournamentDTO) {
        return tournamentRepository.findById(id)
                .map(existingTournament -> {
                    Tournament updatedTournament = tournamentMapper.toEntity(tournamentDTO);
                    updatedTournament.setId(existingTournament.getId());
                    return ResponseEntity.ok(tournamentMapper.toDTO(tournamentRepository.save(updatedTournament)));
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Override
    public ResponseEntity<Void> deleteTournament(Long id) {
        if (tournamentRepository.existsById(id)) {
            tournamentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public Mono<TournamentDTO> createTournamentReactive(TournamentDTO tournamentDTO) {
        return Mono.fromCallable(() -> {
                    entityManager.clear();
                    Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
                    Optional<Tournament> existingTournament = tournamentRepository.findById(tournament.getId());

                    if (existingTournament.isPresent()) {
                        Tournament toUpdate = existingTournament.get();
                        toUpdate.setName(tournament.getName());
                        toUpdate.setDescription(tournament.getDescription());
                        return tournamentRepository.save(toUpdate);
                    } else {
                        return tournamentRepository.save(tournament);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(savedTournament -> tournamentMapper.toDTO(savedTournament));
    }
    public ResponseEntity<List<TournamentDTO>>getLeagueById(Long leagueId, int season) {
        Mono<List<TournamentDTO>> mono = footballApiClient.getLeagueById(leagueId,season)
                .flatMap(jsonNode -> {
                    List<TournamentDTO> tournamentDTOs = extractTournamentsFromJson(jsonNode,season);

                    for (TournamentDTO tournamentDTO : tournamentDTOs) {
                        createTournament(tournamentDTO);
                    }

                    return Mono.just(tournamentDTOs);
                });

        return mono.blockOptional()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(500).build());
    }

    @Override
    public ResponseEntity<List<TournamentDTO>> getActiveTournamentsByDate(LocalDate date) {
        List<Tournament> tournaments = tournamentRepository.findByFinalDateAfter(date);
        return ResponseEntity.ok(tournaments.stream()
                .map(tournamentMapper::toDTO)
                .collect(Collectors.toList()));
    }

    private List<TournamentDTO> extractTournamentsFromJson(JsonNode jsonNode, int season) {
        ArrayNode responseNode = (ArrayNode) jsonNode.get("response");
        if (responseNode == null || responseNode.isEmpty()) {
            throw new RuntimeException("No tournament data found in the response");
        }

        List<TournamentDTO> tournamentDTOs = new ArrayList<>();

        for (JsonNode leagueNode : responseNode) {
            JsonNode leagueInfo = leagueNode.get("league");
            JsonNode seasonsArray  = leagueNode.get("seasons");
            if (seasonsArray == null || !seasonsArray.isArray()) continue;

            JsonNode matchingSeason = null;
            for (JsonNode seasonNode : seasonsArray) {
                if (seasonNode.get("year").asInt() == season) {
                    matchingSeason = seasonNode;
                    break;
                }
            }

            if (matchingSeason == null) continue; // No hay season para ese año

            TournamentDTO dto = tournamentMapper.fromJson(leagueInfo, season, matchingSeason );
            tournamentDTOs.add(dto);

        }

        return tournamentDTOs;
    }

    @Override
    public ResponseEntity<TournamentStatsDTO> updateTournamentStats(Long tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado: " + tournamentId));

        Long leagueId = tournament.getId();
        Long season = 2022L;
        //Long seasonActual = (long) java.time.Year.now().getValue(); // Este es el año actual, solo que no se puede descomentar por falta de API PAGA

        try {
            String standingsJson = apiFootballClient.getStandings(leagueId, season);
            String topScorersJson = apiFootballClient.getTopScorers(leagueId, season);

            TournamentStatistics tournamentStatistics = parseStatsFromJson(leagueId,standingsJson, topScorersJson);
            if(tournament.getTournament_logo()==null || tournament.getTournament_logo().isEmpty()){
                String tournamentImgUrl = updateImgUrl(standingsJson);
                tournament.setTournament_logo(tournamentImgUrl);
                tournamentRepository.save(tournament);
            }
            tournamentStatistics = tournamentStatisticsRepository.save(tournamentStatistics);
            TournamentStatsDTO tournamentStatsDTO = tournamentMapper.toTournamentStatsDTO(
                    tournament,
                    leagueId,
                    tournamentStatistics
            );
            return ResponseEntity.ok(tournamentStatsDTO);
        } catch (Exception e) {
            log.error("Error al actualizar estadísticas del torneo: {}", tournament.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public String getStandings(Long league, Long season) {
        return apiFootballClient.getStandings(league, season);
    }

    private String updateImgUrl(String standingsJson){
        try {
            JsonNode root = objectMapper.readTree(standingsJson);
            JsonNode leagueNode = root.path("response").path(0).path("league");
            String tournamentUrlImg = leagueNode.path("logo").asText();
            return tournamentUrlImg;
        } catch (Exception e) {
            log.error("Error al actualizar la imagen del torneo", e);
            throw new RuntimeException("No se pudó actualizar la imagen dle torneo");
        }
    }

    private TournamentStatistics parseStatsFromJson(Long tournamentId,String standingsJson, String topScorersJson) {
        try {
            JsonNode root = objectMapper.readTree(standingsJson);
            JsonNode leagueNode = root.path("response").path(0).path("league");

            JsonNode standingsArray = leagueNode.path("standings");

            Long winnerTeam = -1L;
            Long topScoringTeam = -1L;
            Long fewestGoalsConcededTeam = -1L;

            Long maxPoints = -1L;
            Long maxGoalsFor = -1L;
            Long minGoalsAgainst = Long.MAX_VALUE;

            for (JsonNode groupNode : standingsArray) {
                for (JsonNode teamNode : groupNode) {
                    Long teamId = teamNode.path("team").path("id").asLong();
                    Long points = teamNode.path("points").asLong();
                    Long goalsFor = teamNode.path("all").path("goals").path("for").asLong();
                    Long goalsAgainst = teamNode.path("all").path("goals").path("against").asLong();

                    if (points > maxPoints) {
                        maxPoints = points;
                        winnerTeam = teamId;
                    }
                    if (goalsFor > maxGoalsFor) {
                        maxGoalsFor = goalsFor;
                        topScoringTeam = teamId;
                    }
                    if (goalsAgainst < minGoalsAgainst) {
                        minGoalsAgainst = goalsAgainst;
                        fewestGoalsConcededTeam = teamId;
                    }
                }
            }

            JsonNode scorersRoot = objectMapper.readTree(topScorersJson);
            JsonNode scorersResponse = scorersRoot.path("response");

            Long topScorerId = -1L;
            String topScorerName = "";
            Long topScorerTeamId = -1L;
            Long maxGoals = -1L;
            String topScorerUrlImg = "";

            for (JsonNode playerNode : scorersResponse) {
                JsonNode statisticsNode = playerNode.path("statistics").get(0);
                Long goals = statisticsNode.path("goals").path("total").asLong();

                if (goals > maxGoals) {
                    maxGoals = goals;
                    topScorerId = playerNode.path("player").path("id").asLong();
                    topScorerName = playerNode.path("player").path("name").asText();
                    topScorerUrlImg = playerNode.path("player").path("photo").asText();
                    topScorerTeamId = statisticsNode.path("team").path("id").asLong();
                }
            }
            return new TournamentStatistics(
                    tournamentId,
                    winnerTeam,
                    fewestGoalsConcededTeam,
                    topScoringTeam,
                    topScorerId,
                    topScorerName,
                    topScorerTeamId,
                    topScorerUrlImg
            );
        } catch (Exception e) {
            log.error("Error al parsear JSON de estadísticas", e);
            throw new RuntimeException("No se pudieron obtener estadísticas del torneo");
        }
    }

}
