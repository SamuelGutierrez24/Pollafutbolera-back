package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

@Builder
public record UserSubpollaDTO(
        Long userId,
        Long subpollaId
) {}
