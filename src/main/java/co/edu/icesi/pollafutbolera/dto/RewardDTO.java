package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record RewardDTO(
        Long id,
        String name,
        String description,
        String image,
        Integer position,
        Long pollaId
) implements Serializable {
}
