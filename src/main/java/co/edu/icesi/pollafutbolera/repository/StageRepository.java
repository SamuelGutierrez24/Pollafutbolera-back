package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Tournament;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    Optional<Stage> findByName(String stageName);
    Optional<Stage> findFirstByTournament_IdAndNameIgnoreCase(Long tournamentId, String keyword);
    List<Stage> findByTournament_IdAndNameContainingIgnoreCase(Long tournamentId, String keyword);
    @Query("SELECT s FROM Stage s WHERE s.tournament.id = :tournamentId")
    List<Stage> findByTournamentId(@Param("tournamentId") Long tournamentId);
}
