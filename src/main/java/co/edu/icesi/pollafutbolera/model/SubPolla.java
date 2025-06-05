package co.edu.icesi.pollafutbolera.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
//@SQLDelete(sql = "UPDATE subpollas SET deleted_at = now() WHERE id = ?")
//@Where(clause = "deleted_at IS NULL")
@Table(name = "subpollas")
public class SubPolla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "private", nullable = false)
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "polla_id", nullable = false)
    private Polla polla;

    @ManyToOne
    @JoinColumn(name = "creator_user_id", nullable = false)
    private User user;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @PreRemove
    public void markAsDeleted() {
        this.deletedAt = new Date();
    }
}