package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.MatchBet;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.User;

import java.time.LocalDateTime;

public class MatchBetUtil {

    public static MatchBet matchBet() {
        User user = User.builder()
                .id(1L)
                .build();

        Polla polla = Polla.builder()
                .id(1L)
                .build();

        Match match = Match.builder()
                .id(1L)
                .build();

        return MatchBet.builder()
                .id(1L)
                .homeScore(2)
                .awayScore(1)
                .earnedPoints(3)
                .user(user)
                .polla(polla)
                .match(match)
                .build();
    }

    public static MatchBetDTO matchBetDTO() {
        return MatchBetDTO.builder()
                .id(1L)
                .homeScore(2)
                .awayScore(1)
                .earnedPoints(3)
                .userId(1L)
                .matchId(1L)
                .pollaId(1L)
                .build();
    }

    public static MatchBetUpdateDTO matchBetUpdateDTO() {
        return MatchBetUpdateDTO.builder()
                .homeScore(3)
                .awayScore(2)
                .build();
    }

    public static MatchBetResponseDTO matchBetResponseDTO() {

        TeamDTO homeTeam = TeamDTO.builder()
                .id(1L)
                .name("Team A")
                .build();

        TeamDTO awayTeam = TeamDTO.builder()
                .id(2L)
                .name("Team B")
                .build();

        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .build();

        MatchEmbeddedDTO matchEmbeddedDTO = MatchEmbeddedDTO.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();

        return MatchBetResponseDTO.builder()
                .id(1L)
                .homeScore(3)
                .awayScore(2)
                .earnedPoints(3)
                .userId(1L)
                .pollaId(1L)
                .match(matchEmbeddedDTO)
                .build();
    }
}