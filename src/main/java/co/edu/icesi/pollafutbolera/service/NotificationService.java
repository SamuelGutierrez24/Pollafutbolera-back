package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface NotificationService {
    Notification createNotification(User user, String content, TypeNotification type);
    Page<Notification> getNotificationsByUser(Long userId, PageRequest pageRequest);
    void markNotificationAsRead(Long notificationId);
    long countUnreadByUser(Long userId);
    void markNotificationsAsRead(java.util.List<Long> notificationIds);
    void markAllAsReadByUser(Long userId);
}
