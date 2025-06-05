package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "match_bets")
public class MatchBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "home_score", nullable = false)
    private Integer homeScore;

    @NotNull
    @Column(name = "away_score", nullable = false)
    private Integer awayScore;

    @Column(name = "earned_points")
    private Integer earnedPoints;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

   @ManyToOne
   @JoinColumn(name = "match_id"  , nullable = false)
   private Match match;

    @ManyToOne
    @JoinColumn(name = "polla_id",  nullable = false)
    private Polla polla;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}