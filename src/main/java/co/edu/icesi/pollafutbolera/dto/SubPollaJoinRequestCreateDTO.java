package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

@Builder
public record SubPollaJoinRequestCreateDTO(
        Long userId,
        Long subpollaId
) {}
