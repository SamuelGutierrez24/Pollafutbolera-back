package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.PreloadedUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreloadedUserRepository extends JpaRepository<PreloadedUser, Long> {

    @Override
    Optional<PreloadedUser> findById(Long aLong);

    List<PreloadedUser> findByMail(String email);

    List<PreloadedUser> findByCedulaAndPolla(@NotNull String cedula, @NotNull Polla polla);

    @Override
    Page<PreloadedUser> findAll(Pageable pageable);

    @Override
    List<PreloadedUser> findAll();

    List<PreloadedUser> findByPollaId(Long pollaId);

    Page<PreloadedUser> findByPollaId(Long pollaId, Pageable pageable);

}
