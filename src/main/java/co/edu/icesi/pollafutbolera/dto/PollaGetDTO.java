package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record PollaGetDTO(
        Long id,
        Date startDate,
        Date endDate,
        RewardDTO[] rewards,
        Boolean isPrivate,
        String imageUrl,
        String color,

        PlatformConfigDTO platformConfig,
        TournamentDTO tournament,
        CompanyDTO company

) {
}
