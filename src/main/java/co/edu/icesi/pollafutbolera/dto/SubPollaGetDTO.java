package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record SubPollaGetDTO(
        Long id,
        boolean isPrivate,
        Long pollaId,
        Long userId
) implements Serializable {
}