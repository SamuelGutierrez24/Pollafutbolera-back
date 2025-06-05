package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findByName(String tournamentName);

    Optional<Tournament> findById(Long id); // Nuevo método para buscar por ID
    @Query("SELECT t FROM Tournament t WHERE t.final_date > :current1_date")
    List<Tournament> findByFinalDateAfter(LocalDate current1_date);

}

