package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixtureSyncService {

    private final FootballService footballService;
    private final TournamentService tournamentService;
    private final UserScoresPollaService userScoresPollaService;
    private final MatchService matchService;
    private final FixtureServiceImpl fixtureService;
    private final StageService stageService;


    @Async
    @Scheduled(cron = "0 50 23 * * *", zone = "America/Bogota")
    public void syncFixturesAtMidnight() {

        System.out.println(" Ejecutando sincronización de fixtures a medianoche...");

        // Define la zona horaria de Colombia
        ZoneId colombiaZone = ZoneId.of("America/Bogota");

        // Obtiene la fecha actual en la zona de Colombia
        LocalDate currentDate = LocalDate.now(colombiaZone);
        List<Integer> tournaments = getAllTournamentsIds(currentDate);

        // Obtiene la hora y fecha actual en Colombia
        LocalDateTime currentDateTime = LocalDateTime.now(colombiaZone);
        LocalDateTime currentDateTimeStart = currentDateTime.toLocalDate().atStartOfDay();
        LocalDateTime currentDateTimeEnd = currentDateTime.toLocalDate().atTime(LocalTime.MAX);
        System.out.println("Fecha y hora actual en Colombia: " + currentDateTime);

        try {
            for (Integer tournament : tournaments) {
                tournamentService.updateTournamentStats(tournament.longValue());

                List<MatchDTO> matches = matchService.findByTournamentAndDateBetween(tournament.longValue(), currentDateTimeStart, currentDateTimeEnd).getBody();
                System.out.println("Holaaaaaaaaaaaaa" + matches);
                if (matches != null) {
                    for (MatchDTO match : matches) {
                        try {
                            footballService.saveMatchById(match.id().intValue());
                            System.out.println("Partido actualizado: " + match.id());

                            updateUserScoresByMatches(match.id());
                            System.out.println("Puntos asignados en el partido: " + match.id());



                            fixtureService.fixtureStatics(match.id());
                            System.out.println("Estadística de partido actualizado: " + match.id());

                        } catch (Exception e) {
                            System.err.println("Error al guardar el partido " + match.id() + ": " + e.getMessage());
                        }

                    }
                }else{
                    System.out.println("No hay partidos para actualizar en la liga " + tournament);
                }
                System.out.println("Sincronización exitosa para liga " + tournament);

                //footballService.saveAllFixtures(tournament, season);


            }
            System.out.println("Sincronización de fixtures a medianoche completada.");

        } catch (Exception e) {
            System.err.println("Error al sincronizar las ligas : " + e.getMessage());
        }


    }


    private List<Integer> getAllTournamentsIds(LocalDate date) {
        List<TournamentDTO> activeTournaments = tournamentService.getActiveTournamentsByDate(date).getBody();
        List<Integer> tournamentIds = new ArrayList<>();
        if (activeTournaments != null) {
            for (TournamentDTO tournament : activeTournaments) {
                tournamentIds.add(tournament.getId().intValue());
            }
            return tournamentIds;
        } else {
            throw new RuntimeException("No se encontraron torneos activos");
        }
    }

    private ResponseEntity<Boolean> updateUserScoresByMatches(Long matchId) {
        try {
            return userScoresPollaService.updateUserScoresByMatch(matchId);
        } catch (Exception e) {
            System.err.println("Error al actualizar los puntajes de los usuarios " + e.getMessage());
            return ResponseEntity.status(500).body(false);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void SyncNewMatchesAtMidnight() {
        System.out.println(" Ejecutando sincronización de nuevos partidos a medianoche...");

        ZoneId colombiaZone = ZoneId.of("America/Bogota");

        // Obtiene la fecha actual en la zona de Colombia
        LocalDate currentDate = LocalDate.now(colombiaZone);
        List<Integer> tournaments = getAllTournamentsIds(currentDate);

        LocalDateTime currentDateTime = LocalDateTime.now(colombiaZone);

        System.out.println("Fecha y hora actual en Colombia: " + currentDateTime);

        try {
            for (Integer tournament : tournaments) {
                List<StageDTO> newStages = stageService.getAllStagesApi(tournament.longValue(),currentDate.getYear()).getBody();
                List<StageDTO> actualStages = stageService.getStagesByTournament(tournament.longValue()).getBody();
                System.out.println("Etapas obtenidas para la liga " + tournament + ": " + actualStages);

                if (newStages != null && !newStages.isEmpty() && actualStages != null) {
                    for (StageDTO stage : newStages) {
                        try {
                            if (!stageExists(stage, actualStages)) {
                                stageService.createStage(stage);
                                System.out.println("Etapa creada: " + stage.id());
                            } else {
                                System.out.println("La etapa " + stage.id() + " ya existe, no se creará de nuevo.");
                            }

                        } catch (Exception e) {
                            System.err.println("Error al crear la etapa " + stage.id() + ": " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("No hay etapas para la liga " + tournament);
                }

                footballService.saveAllFixtures(tournament, currentDate.getYear());
                System.out.println("Sincronización exitosa para liga " + tournament);
            }
            System.out.println("Sincronización de nuevos partidos a medianoche completada.");
        } catch (Exception e) {
            System.err.println("Error al sincronizar las ligas : " + e.getMessage());
        }
    }

    private boolean stageExists(StageDTO stage, List<StageDTO> actualStages) {
        for (StageDTO existingStage : actualStages) {
            if (existingStage.stageName().equals(stage.stageName())) {
                return true; // La etapa ya existe
            }
        }
        return false; // La etapa no existe
    }
}

