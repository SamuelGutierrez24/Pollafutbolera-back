package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_stage_team")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupTeam {

    @EmbeddedId
    private GroupTeamId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group groupStage;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "points")
    private Integer points;

    @Column(name = "rank")
    private Integer rank;
}
