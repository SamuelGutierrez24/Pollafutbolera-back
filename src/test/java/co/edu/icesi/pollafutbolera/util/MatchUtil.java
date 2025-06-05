package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;

import java.time.LocalDateTime;
import java.util.List;

public class MatchUtil {

    public static MatchDTO matchDTO() {
        return MatchDTO.builder()
                .id(1L)
                .date(LocalDateTime.of(2022, 12, 9, 15, 0))
                .status("Match Finished")
                .homeTeamId(10L)
                .awayTeamId(11L)
                .winnerTeamId(11L)
                .tournamentId(11L)
                .stageId(11L)
                .deletedAt(null)
                .homeScore(1)
                .awayScore(1)
                .extratime(false)
                .extraHomeScore(null)
                .extraAwayScore(null)
                .penalty(false)
                .penaltyHome(null)
                .penaltyAway(null)
                .build();
    }
    public static MatchDTO matchDTO2() {
        return MatchDTO.builder()
                .date(LocalDateTime.of(2022, 12, 9, 15, 0))
                .status("Playing")
                .homeTeamId(10L)
                .awayTeamId(11L)
                .winnerTeamId(11L)
                .tournamentId(11L)
                .stageId(11L)
                .deletedAt(null)
                .homeScore(1)
                .awayScore(1)
                .extratime(true)
                .extraHomeScore(1)
                .extraAwayScore(2)
                .penalty(false)
                .penaltyHome(null)
                .penaltyAway(null)
                .build();
    }

    public static List<MatchDTO> matchDTOList() {
        return List.of(
                MatchDTO.builder()
                        .id(1L)
                        .date(LocalDateTime.of(2022, 12, 9, 15, 0))
                        .status("Match Finished")
                        .homeTeamId(24L)
                        .awayTeamId(37L)
                        .winnerTeamId(24L)
                        .tournamentId(3L)
                        .stageId(11L)
                        .deletedAt(null)
                        .homeScore(1)
                        .awayScore(1)
                        .extratime(false)
                        .extraHomeScore(null)
                        .extraAwayScore(null)
                        .penalty(false)
                        .penaltyHome(null)
                        .penaltyAway(null)
                        .build(),
                MatchDTO.builder()
                        .id(2L)
                        .date(LocalDateTime.of(2022, 12, 10, 18, 0))
                        .status("Match Scheduled")
                        .homeTeamId(25L)
                        .awayTeamId(38L)
                        .winnerTeamId(null)
                        .tournamentId(3L)
                        .stageId(12L)
                        .deletedAt(null)
                        .homeScore(0)
                        .awayScore(0)
                        .extratime(false)
                        .extraHomeScore(null)
                        .extraAwayScore(null)
                        .penalty(false)
                        .penaltyHome(null)
                        .penaltyAway(null)
                        .build()
        );
    }
}