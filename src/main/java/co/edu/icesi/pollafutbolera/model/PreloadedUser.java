package co.edu.icesi.pollafutbolera.model;


import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "preloaded_users")
public class PreloadedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "cedula", nullable = false, length = 20)
    private String cedula;


    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @Column(name = "mail", nullable = false, length = 255)
    private String mail;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotNull
    @Column(name = "notifications_enabled")
    private boolean notificationsEnabled;

    @Type(JsonType.class)
    @Column(name = "extra_info", columnDefinition = "json")
    private String extraInfo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "polla_id", nullable = false)
    private Polla polla;

}