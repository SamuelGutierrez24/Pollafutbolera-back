package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMatchRepository extends JpaRepository<GroupMatch, GroupMatchId> {
}
