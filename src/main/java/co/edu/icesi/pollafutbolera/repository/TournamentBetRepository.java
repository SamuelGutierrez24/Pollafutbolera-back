package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.TournamentBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TournamentBetRepository extends JpaRepository<TournamentBet, Long> {

    @Query("SELECT COUNT(tb) > 0 FROM TournamentBet tb WHERE tb.user.id = :userId AND  tb.polla.id = :pollaId")
    boolean existsByUserAndPolla(@Param("userId") Long userId,  @Param("pollaId") Long pollaId);

    @Query("SELECT tb FROM TournamentBet tb WHERE tb.id = :id")
    TournamentBet findTournamentBetById(@Param("id") Long id);

    @Query("SELECT tb FROM TournamentBet tb WHERE tb.user.id = :userId AND tb.polla.id = :pollaId")
    TournamentBet findTournamentBetByUserAndPolla(@Param("userId") Long userId, @Param("pollaId") Long pollaId);

    List<TournamentBet> findByPollaId(Long pollaId);
}
