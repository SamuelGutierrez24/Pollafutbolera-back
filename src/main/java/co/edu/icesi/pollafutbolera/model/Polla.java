    package co.edu.icesi.pollafutbolera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Date;

import java.util.List;



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Entity
    //@SQLDelete(sql = "UPDATE \"pollas\" SET deleted_at = now() WHERE id = ?")
    @Table(name = "pollas")
    @Filter(name = "tenantFilter", condition = "company_id = :tenantId")
    public class Polla {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, insertable = false, updatable = false)
        private Long id;


        @NotNull
        @Column(name = "start_date", nullable = false)
        private Date startDate;

        @NotNull
        @Column(name = "end_date", nullable = false)
        private Date endDate;

        @NotNull
        @Column(name = "private", nullable = false)
        private Boolean isPrivate;

        @Column(name = "image_url")
        private String imageUrl;

        @Column(name = "is_active")
        private Boolean isActive;

        @ManyToOne
        @JoinColumn(name = "company_id")
        private Company company;



        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinColumn(name = "platform_config_id")
        private PlatformConfig platformConfig;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "tournament_id", nullable = false)
        private Tournament tournament;

        @Column(name = "deleted_at")
        @Temporal(TemporalType.TIMESTAMP)
        private Date deletedAt;

        @Column(name = "color")
        private String color;


        @OneToMany(mappedBy = "polla", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        //@JoinColumn(name = "polla_id")
        private List<Reward> rewards;


        @OneToMany(mappedBy = "polla", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<UserScoresPolla> userScores;

        @OneToMany(mappedBy = "polla", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<SubPolla> subPollas;

        @OneToMany(mappedBy = "polla", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<TournamentBet> tournamentBets;

        @OneToMany(mappedBy = "polla", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<MatchBet> matchBets;



        @PreRemove
        public void markAsDeleted() {
            this.deletedAt = new Date();
        }

        public String getPlatformConfigAsString() {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(platformConfig);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
}
