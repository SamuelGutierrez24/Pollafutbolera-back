package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserScoresPollaRepository extends JpaRepository<UserScoresPolla, Long> {

    @Query(value= "SELECT u FROM UserScoresPolla u WHERE u.polla.id = ?1")
    List<UserScoresPolla> findByPollaId(Long pollaId);

    Optional<UserScoresPolla> findByUser_IdAndPolla_Id(Long userId, Long pollaId);

    @Query("SELECT usp FROM UserScoresPolla usp " +
            "WHERE usp.polla.id = :pollaId " +
            "ORDER BY usp.scores DESC")
    List<UserScoresPolla> findTop10ByPollaIdOrderByScoresDesc(Long pollaId, Pageable pageable);

    @Query("SELECT u.polla.id FROM UserScoresPolla u WHERE u.user.id = ?1")
    List<Long> findPollaIdsByUserId(Long userId);

    @Query("SELECT u.polla FROM UserScoresPolla u WHERE u.user.id = ?1")
    List<Polla> findPollaByUserId(Long userId);
    
    @Query("SELECT u FROM UserScoresPolla u INNER JOIN u.polla p " +
            "INNER JOIN SubPolla s on s.polla.id = p.id WHERE " +
            "s.id = ?1 ORDER BY u.scores DESC")
    List<UserScoresPolla> rankingBySubPolla(Long subPollaId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) + 1 FROM " +
            "UserScoresPolla usp WHERE" +
            " usp.polla.id = :pollaId AND usp.scores > " +
            "( SELECT u.scores FROM UserScoresPolla u " +
            "WHERE u.user.id = :userId " +
            "AND u.polla.id = :pollaId)")
    Optional<Long> findRankingByUserIdAndPollaId(Long userId, Long pollaId);
    /*

    */
    @Query(value = "SELECT COUNT(*) + 1 FROM UserScoresPolla usp " +
            "INNER JOIN UserSubPolla ussp ON ussp.user.id = usp.user.id " +
            "WHERE ussp.subpolla.id = :subPollaId " +
            "AND usp.scores > (" +
            "    SELECT u.scores FROM UserScoresPolla u " +
            "    INNER JOIN UserSubPolla us ON us.user.id = u.user.id " +
            "    WHERE us.subpolla.id = :subPollaId AND u.user.id = :userId" +
            ")")
    Optional<Long> findRankingSubPolla(Long userId, Long subPollaId);



    List<UserScoresPolla> findByUser_CedulaAndPolla(String cedula, Polla polla);

}
