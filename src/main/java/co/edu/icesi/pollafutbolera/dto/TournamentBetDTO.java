package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;


@Builder(toBuilder = true)
public record TournamentBetDTO(
        Long id,
        Integer earnedPoints,
        @NotNull(message = "User  is required")
        Long userId,
        @NotNull(message = "Tournament is required")
        Long tournamentId,
        @NotNull(message = "Polla  is required")
        Long pollaId,
        @NotNull(message = "Winner team is required")
        Long winnerTeamId,
        @NotNull(message = "Top scoring team  is required")
        Long topScoringTeamId
) implements Serializable {
}