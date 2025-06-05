package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record MatchDTO(
        Long id,
        @NotNull
        LocalDateTime date,
        @NotNull
        String status,
        @NotNull
        Long homeTeamId,
        @NotNull
        Long awayTeamId,
        Long winnerTeamId,
        @NotNull
        Long tournamentId,
        @NotNull
        Long stageId,
        LocalDateTime deletedAt,
        @NotNull
        Integer homeScore,
        @NotNull
        Integer awayScore,
        @NotNull
        Boolean extratime,
        Integer extraHomeScore,
        Integer extraAwayScore,
        @NotNull
        Boolean penalty,
        Integer penaltyHome,
        Integer penaltyAway
) implements Serializable {

}