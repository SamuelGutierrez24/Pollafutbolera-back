package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<List<Match>> findByStageAndTournament(Stage stage, Tournament tournament);
    Optional<Match> findById(Long id);
    Optional<List<Match> > findByTournament(Tournament tournament);
    List<Match> findByTournamentAndDateBetween(Tournament tournament, LocalDateTime start, LocalDateTime end);
    ;


    Optional<Match> findFirstByTournamentIdAndStageIdAndDateAfterOrderByDateAsc(
            Long tournamentId,
            Long stageId,
            LocalDateTime now
    );

    List<Match> findByTournamentId(Long tournamentId);
}