package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tournament_bets")
public class TournamentBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "earned_points", nullable = false)
    private Integer earnedPoints;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "polla_id")
    private Polla polla;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "winner_team_id")
    private Team winnerTeam;

    @ManyToOne
    @JoinColumn(name = "top_scoring_team_id")
    private Team topScoringTeam;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}