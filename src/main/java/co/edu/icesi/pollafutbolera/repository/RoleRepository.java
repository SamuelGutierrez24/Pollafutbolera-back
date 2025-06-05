package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    Optional<Role> findById(Long id);

    @Override
    Page<Role> findAll(Pageable pageable);

    @Override
    List<Role> findAll();

    Optional<Role> findByName(String name);

}
