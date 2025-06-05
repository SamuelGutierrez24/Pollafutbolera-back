package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.SubPolla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubPollaRepository extends JpaRepository<SubPolla, Long> {
    List<SubPolla> findByPollaId(Long id);
    List<SubPolla> findByUserId(Long userId);

}