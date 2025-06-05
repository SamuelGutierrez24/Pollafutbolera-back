// src/test/java/co/edu/icesi/pollafutbolera/util/TournamentBetUtil.java
package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.model.*;

public class TournamentBetUtil {
    public static TournamentBet tournamentBet() {
        User user = User.builder()
                .id(1L)
                .build();

        Polla polla = Polla.builder()
                .id(1L)
                .build();

        Tournament tournament = Tournament.builder()
                .id(1L)
                .build();

        Team winnerTeam = Team.builder()
                .id(1L)
                .build();

        Team fewestGoalsConcededTeam = Team.builder()
                .id(2L)
                .build();

        Team topScoringTeam = Team.builder()
                .id(3L)
                .build();

        Tournament tournament2 = Tournament.builder()
                .id(1L)
                .winner_team_id(winnerTeam.getId())
                .top_scoring_team_id(topScoringTeam.getId())
                .build();

        return TournamentBet.builder()
                .id(1L)
                .earnedPoints(10)
                .user(user)
                .polla(polla)
                .tournament(tournament2)
                .winnerTeam(winnerTeam)
                .topScoringTeam(topScoringTeam)
                .build();
    }

    public static TournamentBetDTO tournamentBetDTO() {
        return TournamentBetDTO.builder()
                .id(1L)
                .earnedPoints(10)
                .userId(1L)
                .pollaId(1L)
                .tournamentId(1L)
                .winnerTeamId(1L)
                .topScoringTeamId(3L)
                .build();
    }

    public static TournamentBetUpdateDTO tournamentBetUpdateDTO() {
        return TournamentBetUpdateDTO.builder()
                .winnerTeamId(1L)
                .topScoringTeamId(3L)
                .build();
    }
}