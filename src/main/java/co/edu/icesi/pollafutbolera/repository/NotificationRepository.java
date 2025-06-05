package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser_Id(Long userId, Pageable pageable);
    long countByUser_IdAndReadFalse(Long userId);
    void deleteAllByUser_Id(Long userId);
    java.util.List<Notification> findByUser_IdAndReadFalse(Long userId);
}
