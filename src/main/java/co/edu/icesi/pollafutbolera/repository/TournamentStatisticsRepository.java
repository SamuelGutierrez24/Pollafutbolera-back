package co.edu.icesi.pollafutbolera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentStatisticsRepository extends JpaRepository<co.edu.icesi.pollafutbolera.model.TournamentStatistics, Long> {
}
