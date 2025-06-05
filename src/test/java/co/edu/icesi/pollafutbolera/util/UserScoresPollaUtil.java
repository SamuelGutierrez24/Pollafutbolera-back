package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.dto.RankingDTO;
import co.edu.icesi.pollafutbolera.dto.UserScoresPollaDTO;
import co.edu.icesi.pollafutbolera.model.*;

import java.util.List;

public class UserScoresPollaUtil {

    public static UserScoresPolla userScoresPolla() {
        return UserScoresPolla.builder()
                .user(UserUtil.user())
                .polla(PollaUtil.polla())
                .scores(1)
                .build();
    }

    public static UserScoresPolla userScoresPolla2() {
        return UserScoresPolla.builder()
                .user(UserUtil.user2())
                .polla(PollaUtil.polla())
                .scores(2)
                .build();
    }

    public static UserScoresPolla userScoresPolla3() {
        return UserScoresPolla.builder()
                .user(UserUtil.user3())
                .polla(PollaUtil.polla())
                .scores(3)
                .build();
    }

    public static UserScoresPollaDTO userScoresPollaDTO() {
        return UserScoresPollaDTO.builder()
                .userId(1L)
                .pollaId(1L)
                .scores(10)
                .build();
    }

    public static Match createMatch(Long matchId, int homeScore, int awayScore) {
        return Match.builder()
                .id(matchId)
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();
    }

    public static MatchBet createMatchBet(Long matchBetId, Match match, Long pollaId, Long userId, int homeScore, int awayScore) {
        return MatchBet.builder()
                .id(matchBetId)
                .match(match)
                .polla(Polla.builder().id(pollaId).build())
                .user(User.builder().id(userId).build())
                .homeScore(homeScore)
                .awayScore(awayScore)
                .earnedPoints(0)
                .build();
    }

    public static PlatformConfigDTO createPlatformConfig(int exactScorePoints, int matchWinnerPoints) {
        return PlatformConfigDTO.builder()
                .exactScore(exactScorePoints)
                .matchWinner(matchWinnerPoints)
                .build();
    }

    public static UserScoresPolla createUserScoresPolla(User user, Polla polla, int scores) {
        return UserScoresPolla.builder()
                .user(user)
                .polla(polla)
                .scores(scores)
                .build();
    }

    public static PlatformConfigDTO createTournamentPlatformConfig(int tournamentChampion, int teamWithMostGoals) {
        return PlatformConfigDTO.builder()
                .tournamentChampion(tournamentChampion)
                .teamWithMostGoals(teamWithMostGoals)
                .build();
    }

    public static List<TournamentBet> createTournamentBets(TournamentBet tournamentBet) {
        return List.of(tournamentBet);
    }

    public static List<RankingDTO>  createRankingDTOs() {
        return List.of(
                RankingDTO.builder()
                        .user(UserUtil.userDTO())
                        .score(userScoresPollaDTO())
                        .build(),
                RankingDTO.builder()
                        .user(UserUtil.userDTO())
                        .score(userScoresPollaDTO())
                        .build()
        );
    }

}
