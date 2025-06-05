package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record StageDTO (
        Long id,
        @NotNull
        String stageName,
        @NotNull
        Long tournamentId,
        LocalDateTime deletedAt
)implements Serializable {
}