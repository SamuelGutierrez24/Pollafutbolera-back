package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record TournamentStatsDTO (
    Long id,
    String tournamentName,
    String description,
    String tournament_logo,
    Long winnerTeam,
    Long fewestGoalsConcededTeam,
    Long topScoringTeam,
    Long topScorerId,
    String topScorerName,
    Long topScorerTeamId,
    String topScorerImg
) implements Serializable {
}