package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_subpolla")
@IdClass(UserSubpollaId.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubPolla {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "subpolla_id", nullable = false)
    private SubPolla subpolla;
}
