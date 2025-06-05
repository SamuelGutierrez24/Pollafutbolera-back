package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.model.TypeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeNotificationRepository extends JpaRepository<TypeNotification, Long> {
    TypeNotification findByName(String name);
}
