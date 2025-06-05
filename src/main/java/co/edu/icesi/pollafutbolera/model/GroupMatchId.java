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
public class GroupMatchId implements Serializable {
    private Long groupId;
    private Long matchId;
}
