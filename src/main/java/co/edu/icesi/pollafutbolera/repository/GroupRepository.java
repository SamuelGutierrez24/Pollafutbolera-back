package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Group;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<List<Group>> findByGroupNameAndTournamentId(String name,Long id);
    @Query("select distinct g from Group g " +
            "left join fetch g.teams " +
            "left join fetch g.matches " +
            "where g.deletedAt is null")
    List<Group> findAllWithDetails();

    @Query("""
       SELECT DISTINCT g
       FROM   Group g
              LEFT JOIN FETCH g.groupTeams gt
              LEFT JOIN FETCH gt.team t
              LEFT JOIN FETCH g.matches m
       WHERE  g.tournamentId = :tournamentId
       """)
    List<Group> findAllWithDetailsByTournamentId(Long tournamentId);
}
