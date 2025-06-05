package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    Boolean existsByNickname(String nickname);

    List<User> findByCedula(String cedula);

    List<User> findByMail(String email);

    List<User> findUsersByNotificationsEmailEnabled(boolean notificationsEmailEnabled);

    List<User> findUsersByNotificationsSMSEnabled(boolean notificationsSMSEnabled);

    List<User> findUsersByNotificationsWhatsappEnabled(boolean notificationsWhatsappEnabled);

    Optional<User> findByNickname(String username);

    @Override
    Page<User> findAll(Pageable pageable);

    @Override
    List<User> findAll();
}
