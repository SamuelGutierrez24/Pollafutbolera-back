package co.edu.icesi.pollafutbolera.repository;
import co.edu.icesi.pollafutbolera.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String teamName);
    @Query(value = "SELECT * FROM public.\"teams\" WHERE external_id = :externalId AND deleted_at IS NULL", nativeQuery = true)
    Optional<Team> findByExternalId(@Param("externalId") Long externalId);
}

