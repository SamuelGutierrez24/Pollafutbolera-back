package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.model.Team;

import java.util.List;

public class TeamUtil {

    public static Team team() {
        return Team.builder()
                .id(1L)
                .name("Team A")
                .logoUrl("https://example.com/logoA.png")
                .build();
    }

    public static List<Team> teamList() {
        return List.of(
                Team.builder()
                        .id(1L)
                        .name("Team A")
                        .logoUrl("https://example.com/logoA.png")
                        .build(),
                Team.builder()
                        .id(2L)
                        .name("Team B")
                        .logoUrl("https://example.com/logoB.png")
                        .build()
        );
    }
}