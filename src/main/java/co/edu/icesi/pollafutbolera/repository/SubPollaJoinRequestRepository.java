package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.SubPollaJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubPollaJoinRequestRepository extends JpaRepository<SubPollaJoinRequest, Long> {
    Optional<SubPollaJoinRequest> findById(Long id);

    List<SubPollaJoinRequest> findBySubpollaId(Long subpollaId);

}
