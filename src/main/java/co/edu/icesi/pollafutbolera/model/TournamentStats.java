package co.edu.icesi.pollafutbolera.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentStats {
    private String tournamentName;
    private Long winnerTeam;
    private Long fewestGoalsConcededTeam;
    private Long topScoringTeam;
    private Long topScorerId;         // Nueva propiedad para el mejor goleador
    private String topScorerName;    // Nombre del mejor goleador
    private Long topScorerTeamId;      // Id del equipo del mejor goleador
}
