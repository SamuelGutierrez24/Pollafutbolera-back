package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.UserSubPolla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserSubPollaRepository extends JpaRepository<UserSubPolla, Long> {
    List<UserSubPolla> findBySubpolla_Id(Long subpollaId);
}
