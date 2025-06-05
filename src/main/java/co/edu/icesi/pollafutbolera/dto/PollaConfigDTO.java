package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.model.Company;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
public record PollaConfigDTO (
        @NotNull
        Date startDate,
        @NotNull
        Date endDate,
        @NotNull
        Boolean isPrivate,
        String color,
        String imageUrl,
        PlatformConfigDTO platformConfig,
        Long tournamentId,
        List<RewardDTO> rewards,
        Long company

) implements Serializable {
}