package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.NotificationAPI;
import co.edu.icesi.pollafutbolera.dto.NotificationDTO;
import co.edu.icesi.pollafutbolera.mapper.NotificationMapper;
import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class NotificationController implements NotificationAPI {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final PagedResourcesAssembler<NotificationDTO> pagedResourcesAssembler;

    @Override
    public ResponseEntity<PagedModel<NotificationDTO>> getUserNotifications(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Notification> notifications = notificationService.getNotificationsByUser(userId, pageRequest);
        Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
        PagedModel<NotificationDTO> pagedModel = pagedResourcesAssembler.toModel(dtoPage, notificationDTO -> notificationDTO);
        return ResponseEntity.ok(pagedModel);
    }

    @Override
    public ResponseEntity<Void> markAsRead(Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.countUnreadByUser(userId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsReadByUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> markBatchAsRead(@PathVariable Long userId, @RequestBody java.util.List<Long> notificationIds) {
        notificationService.markNotificationsAsRead(notificationIds);
        return ResponseEntity.noContent().build();
    }
}
