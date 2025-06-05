package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SQLDelete(sql = "UPDATE matches SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Match {
    //TODO: Add missing fields defined in the schema: score and scorers
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name= "date" ,nullable = false)
    private LocalDateTime date;

    @Column(name="status",length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_match_home_team"))
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_match_away_team"))
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "winner_team_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_match_winner_team"))
    private Team winnerTeam;

    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_match_tournament"))
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "stage_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_match_stage"))
    private Stage stage;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "extra_time")
    private Boolean extratime;

    @Column(name = "extra_home_score")
    private Integer extraHomeScore;

    @Column(name = "extra_away_score")
    private Integer extraAwayScore;

    @Column(name = "penalty")
    private Boolean penalty;

    @Column(name = "penalty_home")
    private Integer penaltyHome;

    @Column(name = "penalty_away")
    private Integer penaltyAway;

}