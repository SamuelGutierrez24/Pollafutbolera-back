package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.NotificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Notification createNotification(User user, String content, TypeNotification type) {
        Notification notification = Notification.builder()
                .user(user)
                .content(content)
                .timestamp(LocalDateTime.now())
                .type(type)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsByUser(Long userId, PageRequest pageRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> query = cb.createQuery(Notification.class);
        Root<Notification> root = query.from(Notification.class);

        root.fetch("type", JoinType.LEFT);

        query.where(cb.equal(root.get("user").get("id"), userId));

        if (pageRequest.getSort().isSorted()) {
            query.orderBy(cb.desc(root.get("timestamp")));
        }

        List<Notification> notifications = entityManager.createQuery(query)
                .setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Notification> countRoot = countQuery.from(Notification.class);
        countQuery.select(cb.count(countRoot))
                .where(cb.equal(countRoot.get("user").get("id"), userId));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(notifications, pageRequest, count);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByUser(Long userId) {
        return notificationRepository.countByUser_IdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markNotificationsAsRead(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void markAllAsReadByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_IdAndReadFalse(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
}