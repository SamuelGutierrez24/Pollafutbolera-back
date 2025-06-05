package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record PlatformConfigDTO (
        Long id,
        Integer tournamentChampion,
        Integer teamWithMostGoals,
        Integer exactScore,
        Integer matchWinner
) implements Serializable {
}
