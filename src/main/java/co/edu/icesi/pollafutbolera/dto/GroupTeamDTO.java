package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record GroupTeamDTO(
        @NotNull Long teamId,
        @NotNull Integer rank,
        @NotNull Integer points,
        @NotNull String teamName,
        @NotNull String teamLogoUrl
) implements Serializable {
}