package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "platform_config")
public class PlatformConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "tournament_champion")
    private Integer tournamentChampion;

    @Column(name = "team_with_most_goals")
    private Integer teamWithMostGoals;

    @Column(name = "exact_score")
    private Integer exactScore;

    @Column(name = "match_winner")
    private Integer matchWinner;
}