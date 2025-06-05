package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record MatchBetUpdateDTO (
        @NotNull(message = "Home score is required")
        Integer homeScore,
        @NotNull(message = "Away Score score is required")
        Integer awayScore
)implements Serializable {
}