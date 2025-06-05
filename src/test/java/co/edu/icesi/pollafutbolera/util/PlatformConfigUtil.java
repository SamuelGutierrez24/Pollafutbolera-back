package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.model.PlatformConfig;

public class PlatformConfigUtil {

    public static PlatformConfig createPlatformConfig() {
        return PlatformConfig.builder()
                .tournamentChampion(1)
                .teamWithMostGoals(2)
                .exactScore(3)
                .matchWinner(5)
                .build();
    }

    public static PlatformConfigDTO createPlatformConfigDTO() {
        return PlatformConfigDTO.builder()
                .tournamentChampion(1)
                .teamWithMostGoals(2)
                .exactScore(3)
                .matchWinner(5)
                .build();
    }
}