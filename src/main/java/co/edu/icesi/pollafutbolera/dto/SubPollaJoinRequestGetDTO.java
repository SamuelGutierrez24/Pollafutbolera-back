package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.enums.SubPollaJoinRequestStatus;
import lombok.Builder;

@Builder
public record SubPollaJoinRequestGetDTO(
        Long id,
        Long userId,
        Long subpollaId,
        SubPollaJoinRequestStatus status
) {}