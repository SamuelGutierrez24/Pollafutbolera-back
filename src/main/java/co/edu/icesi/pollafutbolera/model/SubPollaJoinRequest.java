package co.edu.icesi.pollafutbolera.model;

import co.edu.icesi.pollafutbolera.enums.SubPollaJoinRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subpolla_join_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubPollaJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subpolla_id", nullable = false)
    private SubPolla subpolla;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubPollaJoinRequestStatus status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
