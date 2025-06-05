package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record RankingDTO(
        UserDTO user,
        UserScoresPollaDTO score
) implements Serializable {
}
