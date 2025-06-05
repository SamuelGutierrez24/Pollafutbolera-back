package co.edu.icesi.pollafutbolera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
//@SQLDelete(sql = "UPDATE companies SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
//@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @NotNull
    @Size(max = 100)
    @Column(name = "nit", nullable = false, length = 100, unique = true)
    private String nit;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 100)
    @Column(name = "contact", length = 100)
    private String contact;

    @Size(max = 150)
    @Column(name = "logo", length = 150, nullable = false)
    private String logo;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Polla> pollas = new ArrayList<>();

    public Company(Long id, String name, String nit, String address, String contact, String logo, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.nit = nit;
        this.address = address;
        this.contact = contact;
        this.logo = logo;
        this.deletedAt = deletedAt;
    }
}