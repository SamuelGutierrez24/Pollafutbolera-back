package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record MatchEmbeddedDTO(
        Long id,
        @NotNull
        LocalDateTime date,
        @NotNull
        String status,
        @NotNull
        TeamDTO homeTeam,
        @NotNull
        TeamDTO awayTeam
) implements Serializable {

}