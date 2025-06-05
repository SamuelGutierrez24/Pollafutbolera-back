package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tournament_statistics", schema = "public")
public class TournamentStatistics {

    @Id
    @Column(name = "tournament_id", nullable = false, updatable = false)
    private Long tournamentId;

    @Column(name = "winner_team_id")
    private Long winnerTeamId;

    @Column(name = "fewest_goals_conceded_team_id")
    private Long fewestGoalsConcededTeamId;

    @Column(name = "top_scoring_team_id")
    private Long topScoringTeamId;

    @Column(name = "top_scorer_id")
    private Long topScorerId;

    @Column(name = "top_scorer_name", length = 255)
    private String topScorerName;

    @Column(name = "top_scorer_team_id")
    private Long topScorerTeamId;

    @Column(name = "top_scorer_urlimg", columnDefinition = "TEXT")
    private String topScorerUrlImg;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    public TournamentStatistics(Long tournamentId, Long winnerTeam, Long fewestGoalsConcededTeam, Long topScoringTeam, Long topScorerId, String topScorerName, Long topScorerTeamId, String topScorerUrlImg) {
        this.tournamentId = tournamentId;
        this.winnerTeamId = winnerTeam;
        this.fewestGoalsConcededTeamId = fewestGoalsConcededTeam;
        this.topScoringTeamId = topScoringTeam;
        this.topScorerId = topScorerId;
        this.topScorerName = topScorerName;
        this.topScorerTeamId = topScorerTeamId;
        this.topScorerUrlImg = topScorerUrlImg;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
