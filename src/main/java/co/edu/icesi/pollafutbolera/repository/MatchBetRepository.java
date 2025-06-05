package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.MatchBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchBetRepository extends JpaRepository<MatchBet, Long> {

    @Query("SELECT mb FROM MatchBet mb WHERE mb.id = :id")
    MatchBet findMatchBetById(@Param("id") Long id);

    @Query("SELECT mb FROM MatchBet mb WHERE mb.user.id = :userId")
    List<MatchBet> findMatchBetsByUser(@Param("userId") Long userId);

    @Query("SELECT mb FROM MatchBet mb WHERE mb.user.id = :userId AND mb.polla.id = :pollaId")
    List<MatchBet> findMatchBetsByUserAndPolla(@Param("userId") Long userId, @Param("pollaId") Long pollaId);

    @Query("SELECT mb FROM MatchBet mb WHERE mb.match.id = :matchId ORDER BY mb.polla.id")
    List<MatchBet> findByMatch(@Param("matchId") Long matchId);


    @Query("SELECT COUNT(mb) > 0 FROM MatchBet mb WHERE mb.user.id = :userId AND mb.match.id = :matchId AND mb.polla.id = :pollaId")
    boolean existsByUserAndMatchAndPolla(@Param("userId") Long userId, @Param("matchId") Long matchId, @Param("pollaId") Long pollaId);
}
