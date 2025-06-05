package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_stage_match")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMatch {

    @EmbeddedId
    private GroupMatchId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group groupStage;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;
}
