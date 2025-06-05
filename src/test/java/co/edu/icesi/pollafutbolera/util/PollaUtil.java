package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Reward;
import co.edu.icesi.pollafutbolera.model.Tournament;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class PollaUtil {
    public static Polla polla() {
        return Polla.builder()
                .id(4L)
                .startDate(Date.from(Instant.parse("2027-10-10T00:00:00Z")))
                .endDate(Date.from(Instant.parse("2027-11-10T00:00:00Z")))
                .imageUrl("https://www.google.com")
                .rewards(List.of(new Reward(1L, "Reward 1", "Description 1", "image1.png", 1, null)))
                .isPrivate(true)
                .platformConfig(PlatformConfigUtil.createPlatformConfig())
                .color("blue") // Set color field
                .build();
    }

    public static Polla futurePolla(){
        return Polla.builder()
                .id(4L)
                .startDate(Date.from(Instant.parse("2027-10-10T00:00:00Z")))
                .endDate(Date.from(Instant.parse("2027-11-10T00:00:00Z")))
                .imageUrl("https://www.google.com")
                .rewards(List.of(new Reward(1L, "Reward 1", "Description 1", "image1.png", 1, null)))
                .isPrivate(true)
                .platformConfig(PlatformConfigUtil.createPlatformConfig())
                .color("blue")
                .build();
    }

    public static Tournament tournament = Tournament.builder()
            .id(1L)
                .name("Champions League")
                .description("Top European competition")
                .winner_team_id(TeamUtil.team().getId())
                .fewest_goals_conceded_team_id(TeamUtil.team().getId())
                .top_scoring_team_id(TeamUtil.team().getId())
                .build();

    public static TournamentDTO tournamentConfigDTO  = new TournamentDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDescription(),
            null,
            null,
            tournament.getWinner_team_id(),
            tournament.getFewest_goals_conceded_team_id(),
            tournament.getTop_scoring_team_id(),
            null
    );


    public static PollaConfigDTO pollaConfigDTO = new PollaConfigDTO(
            Date.from(Instant.parse("2027-10-10T00:00:00Z")),
            Date.from(Instant.parse("2027-11-10T00:00:00Z")),
            true,
            "blue",
            "https://www.google.com",
            PlatformConfigUtil.createPlatformConfigDTO(),
            10L,
            RewardUtil.rewardDTOs(),
            10L


    );

    public static PollaGetDTO pollaGetDTO = new PollaGetDTO(
            11L,
            Date.from(Instant.parse("2027-10-10T00:00:00Z")),
            Date.from(Instant.parse("2027-11-10T00:00:00Z")),
            List.of(new RewardDTO(1L, "Reward 1", "Description 1", "image1.png", 1, 5L)).toArray(new RewardDTO[0]),
            true,
            "https://www.google.com",
            "blue",
            PlatformConfigUtil.createPlatformConfigDTO(),
            tournamentConfigDTO,
            CompanyUtil.companyDTO()
    );

    public static PollaGetDTO pollaGetDTO() {
        return PollaGetDTO.builder()
                .id(11L)
                .startDate(Date.from(Instant.parse("2027-10-10T00:00:00Z")))
                .endDate(Date.from(Instant.parse("2027-11-10T00:00:00Z")))
                .isPrivate(true)
                .imageUrl("https://www.google.com")
                .platformConfig(PlatformConfigUtil.createPlatformConfigDTO())
                .rewards(RewardUtil.rewardDTOs().toArray(new RewardDTO[0]))
                .build();
    }
}
