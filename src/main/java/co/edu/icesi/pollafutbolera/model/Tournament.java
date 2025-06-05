package co.edu.icesi.pollafutbolera.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tournaments")
@Builder
public class Tournament {

    @Id
    @Column(name = "id",nullable = false, insertable=true, updatable=false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "winner_team_id")
    private Long winner_team_id;

    @Column(name = "fewest_goals_conceded_team_id")
    private Long fewest_goals_conceded_team_id;

    @Column(name = "top_scoring_team_id")
    private Long top_scoring_team_id;

    @Column(name = "final_date")
    private LocalDate final_date;
    
    @Column(name= "tournament_logo", columnDefinition = "TEXT")
    private String tournament_logo;
    
    @Column(name = "start_date")
    private LocalDate initial_date;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deleted_at;
}
