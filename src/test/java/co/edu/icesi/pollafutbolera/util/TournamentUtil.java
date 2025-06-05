package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TournamentUtil {

    public static Tournament tournament() {
        return Tournament.builder()
                .id(1L)
                .name("Torneo de Colombia")
                .description("Torneo de Colombia")
                .winner_team_id(1L)
                .fewest_goals_conceded_team_id(2L)
                .top_scoring_team_id(3L)
                .build();
    }

    public static Team createTeam(Long id, String name) {
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        return team;
    }

    public static TournamentDTO createTournamentDTO() {
        return new TournamentDTO(
                1L,
                "World Cup",
                "International football competition",
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 12, 10),
                1L,
                2L,
                3L,
                null
        );
    }
}