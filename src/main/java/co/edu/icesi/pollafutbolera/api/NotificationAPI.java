package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.NotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification API", description = "API for managing notifications")
@RequestMapping("/notifications")
public interface NotificationAPI {
    @Operation(summary = "Get notifications for a user, paginated and ordered descending by timestamp")
    @GetMapping("/user/{userId}")
    ResponseEntity<PagedModel<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Mark a notification as read")
    @PutMapping("/{notificationId}/read")
    ResponseEntity<Void> markAsRead(@PathVariable Long notificationId);

    @Operation(summary = "Get unread notification count for a user")
    @GetMapping("/user/{userId}/unread/count")
    ResponseEntity<Long> getUnreadCount(@PathVariable Long userId);

    @Operation(summary = "Mark all notifications as read for a user")
    @PutMapping("/user/{userId}/read")
    ResponseEntity<Void> markAllAsRead(@PathVariable Long userId);

    @Operation(summary = "Mark a batch of notifications as read for a user")
    @PutMapping("/user/{userId}/read/batch")
    ResponseEntity<Void> markBatchAsRead(@PathVariable Long userId, @RequestBody List<Long> notificationIds);
}
