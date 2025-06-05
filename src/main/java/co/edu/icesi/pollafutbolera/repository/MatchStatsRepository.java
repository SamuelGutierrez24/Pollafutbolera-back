package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.MatchStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchStatsRepository extends JpaRepository<MatchStats,Long> {
    List<MatchStats> findByMatchId(Match matchId);
}
