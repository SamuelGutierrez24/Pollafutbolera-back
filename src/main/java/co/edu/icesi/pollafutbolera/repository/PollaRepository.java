package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Polla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PollaRepository extends JpaRepository<Polla, Long> {
    @Query("SELECT p FROM Polla p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Polla> findById(Long id);

    @Query("SELECT p FROM Polla p WHERE p.deletedAt IS NULL")
    List<Polla> findAllActive();

    @Query("SELECT p FROM Polla p WHERE p.company.id = :companyId AND p.deletedAt IS NULL AND p.isActive = true")
    List<Polla> findbyCompanyId(Long companyId);
}
