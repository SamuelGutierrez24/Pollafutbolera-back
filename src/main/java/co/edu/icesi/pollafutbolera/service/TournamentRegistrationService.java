package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentRegistrationService {

    private final TournamentService tournamentService;
    private final StageService stageService;
    private final FootballService footballService;
    private final TeamService teamService;

    public ResponseEntity<TournamentDTO> registerTournament(Long leagueId, int season) throws Exception{
        try {
            ResponseEntity<List<TournamentDTO>> tournamentResponse = tournamentService.getLeagueById(leagueId, season);
            if (tournamentResponse.getStatusCode().isError() || tournamentResponse.getBody() == null || tournamentResponse.getBody().isEmpty()) {
                return ResponseEntity.status(500).body(null);
            }

            // Crear etapas para el torneo
            TournamentDTO savedTournament = tournamentResponse.getBody().get(0);
            ResponseEntity<List<StageDTO>> stagesResponse = stageService.getAllStagesApi(leagueId, season);

            if (stagesResponse.getStatusCode().isError()) {
                throw new Exception("Error saving stages");
            }

            try {
                ResponseEntity<Void> teams =  teamService.fetchAndSaveTeams(leagueId.intValue(), season);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ResponseEntity<List<MatchDTO>> matches = footballService.saveAllFixtures(leagueId.intValue(), season);

            if(matches.getStatusCode().isError() || matches.getBody() == null || matches.getBody().isEmpty()) {
                throw new Exception("Error saving matches");
            }

            return ResponseEntity.ok(savedTournament);
        } catch (Exception e) {
            System.out.println("Error registering tournament: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}