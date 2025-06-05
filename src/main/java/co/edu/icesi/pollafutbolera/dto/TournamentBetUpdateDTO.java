package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record TournamentBetUpdateDTO(
        @NotNull(message = "Winner team  is required")
        Long winnerTeamId,
        @NotNull(message = "Top scoring team  is required")
        Long topScoringTeamId
) implements Serializable {
}