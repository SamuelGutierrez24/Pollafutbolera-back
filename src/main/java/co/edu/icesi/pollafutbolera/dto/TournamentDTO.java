package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.model.Polla;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TournamentDTO implements Serializable {
    private Long id;

    private String name;

    private String description;

    private LocalDate initial_date;

    private LocalDate final_date;

    private Long winner_team_id;

    private Long fewest_goals_conceded_team_id;

    private Long top_scoring_team_id;

    private LocalDate deleted_at;
}
