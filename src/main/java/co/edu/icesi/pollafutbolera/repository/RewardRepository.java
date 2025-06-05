package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<List<Reward>> findByPollaId(Long pollaId);
}
