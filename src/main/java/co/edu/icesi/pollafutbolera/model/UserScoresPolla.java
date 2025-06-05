package co.edu.icesi.pollafutbolera.model;

import co.edu.icesi.pollafutbolera.enums.UserPollaState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_scores_polla")
@IdClass(UserScoresPollaId.class)
public class UserScoresPolla {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",  nullable = false)
    @JsonIgnoreProperties({
            "scoresPolla",
            "company",
            "role",
            "password",
            "deletedAt"
    })
    private User user;

    @ManyToOne
    @JoinColumn(name = "polla_id", referencedColumnName = "id", nullable = false)
    @Id
    @JsonIgnoreProperties({
            "userScores",
            "company",
            "platformConfig",
            "tournament",
            "rewards",
            "deletedAt"
    })
    private Polla polla;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPollaState state;

    @Column(nullable = false)
    private int scores;

}