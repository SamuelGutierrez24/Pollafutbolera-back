package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupTeamId implements Serializable {
    private Long groupId;
    private Long teamId;
}
