package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Group;
import co.edu.icesi.pollafutbolera.model.GroupTeam;
import co.edu.icesi.pollafutbolera.model.GroupTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupTeamRepository extends JpaRepository<GroupTeam, GroupTeamId> {
    @Query("SELECT gt FROM GroupTeam gt WHERE gt.id.teamId = :teamId")
    List<GroupTeam> findByTeamId(@Param("teamId") Long teamId);
}
