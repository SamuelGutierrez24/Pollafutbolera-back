package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.PlatformConfig;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlatformConfigRepository extends JpaRepository<PlatformConfig, Long> {

    @Override
    Optional<PlatformConfig> findById(Long id);

    @Override
    Page<PlatformConfig> findAll(Pageable pageable);

    @Override
    List<PlatformConfig> findAll();

}


