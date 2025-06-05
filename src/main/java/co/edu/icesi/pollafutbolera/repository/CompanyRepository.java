package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Override
    Optional<Company> findById(Long aLong);

    Boolean existsByNit(String nit);

    List<Company> findByNit(String nit);

    @Override
    Page<Company> findAll(Pageable pageable);

    Optional<Company> findByName(String name);

    @Override
    List<Company> findAll();
}
