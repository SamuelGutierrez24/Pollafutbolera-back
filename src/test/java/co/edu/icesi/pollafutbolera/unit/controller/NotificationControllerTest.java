package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.NotificationController;
import co.edu.icesi.pollafutbolera.dto.NotificationDTO;
import co.edu.icesi.pollafutbolera.mapper.NotificationMapper;
import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private PagedResourcesAssembler<NotificationDTO> pagedResourcesAssembler;

    @InjectMocks
    private NotificationController notificationController;

    private Notification notification;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notificationDTO = NotificationDTO.builder()
                .id(1L)
                .userId(1L)
                .content("Test notification")
                .timestamp(LocalDateTime.now())
                .typeId(1L)
                .typeName("Test Type")
                .read(false)
                .build();
    }

    @Test
    void getUserNotifications_ShouldReturnPagedModel() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Notification> notifications = new PageImpl<>(Collections.singletonList(notification));
        Page<NotificationDTO> dtoPage = new PageImpl<>(Collections.singletonList(notificationDTO));
        PagedModel<NotificationDTO> pagedModel = PagedModel.empty();

        when(notificationService.getNotificationsByUser(userId, pageRequest)).thenReturn(notifications);
        when(notificationMapper.toDTO(notification)).thenReturn(notificationDTO);
        when(pagedResourcesAssembler.toModel(any(Page.class), any(RepresentationModelAssembler.class))).thenReturn(pagedModel);

        ResponseEntity<PagedModel<NotificationDTO>> response = notificationController.getUserNotifications(userId, page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagedModel, response.getBody());
    }

    @Test
    void markAsRead_ShouldReturnNoContent() {
        Long notificationId = 1L;
        doNothing().when(notificationService).markNotificationAsRead(notificationId);
        ResponseEntity<Void> response = notificationController.markAsRead(notificationId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).markNotificationAsRead(notificationId);
    }

    @Test
    void getUnreadCount_ShouldReturnCount() {
        Long userId = 1L;
        long count = 5L;
        when(notificationService.countUnreadByUser(userId)).thenReturn(count);
        ResponseEntity<Long> response = notificationController.getUnreadCount(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(count, response.getBody());
    }

    @Test
    void markAllAsRead_ShouldReturnNoContent() {
        Long userId = 1L;
        doNothing().when(notificationService).markAllAsReadByUser(userId);
        ResponseEntity<Void> response = notificationController.markAllAsRead(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).markAllAsReadByUser(userId);
    }

    @Test
    void markBatchAsRead_ShouldReturnNoContent() {
        Long userId = 1L;
        List<Long> notificationIds = Arrays.asList(1L, 2L, 3L);
        doNothing().when(notificationService).markNotificationsAsRead(notificationIds);
        ResponseEntity<Void> response = notificationController.markBatchAsRead(userId, notificationIds);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).markNotificationsAsRead(notificationIds);
    }
}
