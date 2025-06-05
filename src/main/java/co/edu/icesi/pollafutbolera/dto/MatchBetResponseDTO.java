package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.model.Match;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record MatchBetResponseDTO (
        Long id,
        @NotNull(message = "Home score is required")
        Integer homeScore,
        @NotNull(message = "Away Score score is required")
        Integer awayScore,
        Integer earnedPoints,
        @NotNull(message = "User is required")
        Long userId,
        @NotNull(message = "Polla is required")
        Long pollaId,
        @NotNull(message = "Match is required")
        MatchEmbeddedDTO match
) implements Serializable {
}
