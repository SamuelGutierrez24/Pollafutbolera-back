package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Override
    Optional<Permission> findById(Long id);

    @Override
    Page<Permission> findAll(Pageable pageable);

    @Override
    List<Permission> findAll();

    Optional<Permission> findByName(String name);

}
