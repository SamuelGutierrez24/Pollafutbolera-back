package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record SubPollaJoinRequestResponseDTO(
        String decision
) implements Serializable {
}
