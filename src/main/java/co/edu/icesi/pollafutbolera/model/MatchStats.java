package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "match_statistics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class MatchStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "team_name", nullable = true, updatable = true)
    private String teamName;

    @Column(name = "logo", nullable = true, updatable = true)
    private String logo;

    @OneToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    private Match matchId;

    @Column(name = "shots_on_goal", nullable = true, updatable = true)
    private Integer shotsOnGoal;

    @Column(name = "shots_off_goal", nullable = true, updatable = true)
    private Integer shotsOffGoal;

    @Column(name = "total_shots", nullable = true, updatable = true)
    private Integer totalShots;

    @Column(name = "blocked_shots", nullable = true, updatable = true)
    private Integer blockedShots;

    @Column(name = "shots_inside_box", nullable = true, updatable = true)
    private Integer shotsInsidebox;

    @Column(name = "shots_outside_box", nullable = true, updatable = true)
    private Integer shotsOutsidebox;

    @Column(name = "fouls", nullable = true, updatable = true)
    private Integer fouls;

    @Column(name = "corner_kicks", nullable = true, updatable = true)
    private Integer cornerKicks;

    @Column(name = "offsides", nullable = true, updatable = true)
    private Integer offsides;

    @Column(name = "ball_possession", nullable = true, updatable = true, length = 10)
    private String ballPossession;

    @Column(name = "yellow_cards", nullable = true, updatable = true)
    private Integer yellowCards;

    @Column(name = "red_cards", nullable = true, updatable = true)
    private Integer redCards;

    @Column(name = "goalkeeper_saves", nullable = true, updatable = true)
    private Integer goalkeeperSaves;

    @Column(name = "total_passes", nullable = true, updatable = true)
    private Integer totalPasses;

    @Column(name = "passes_accurate", nullable = true, updatable = true)
    private Integer passesAccurate;

    @Column(name = "passes_percentage", nullable = true, updatable = true, length = 10)
    private String passesPercentage;

}
