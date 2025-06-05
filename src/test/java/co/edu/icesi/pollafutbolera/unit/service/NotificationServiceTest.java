package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.NotificationRepository;
import co.edu.icesi.pollafutbolera.service.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private TypeNotification type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        type = new TypeNotification("TEST", "Test notification");
        type.setId(1L);
    }

    @Test
    void testCreateNotification() {
        String content = "Test content";
        Notification notification = Notification.builder()
                .user(user)
                .content(content)
                .timestamp(java.time.LocalDateTime.now())
                .type(type)
                .build();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification created = notificationService.createNotification(user, content, type);
        assertNotNull(created);
        assertEquals(content, created.getContent());
        assertEquals(user, created.getUser());
        assertEquals(type, created.getType());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkNotificationAsRead_Success() {
        Notification notification = Notification.builder()
                .id(1L)
                .user(user)
                .read(false)
                .build();
        when(notificationRepository.findById(1L)).thenReturn(java.util.Optional.of(notification));
        notificationService.markNotificationAsRead(1L);
        assertTrue(notification.isRead());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testMarkNotificationsAsRead_Batch() {
        Notification notif1 = Notification.builder().id(1L).user(user).read(false).build();
        Notification notif2 = Notification.builder().id(2L).user(user).read(false).build();
        java.util.List<Long> ids = java.util.Arrays.asList(1L, 2L);
        java.util.List<Notification> notifs = java.util.Arrays.asList(notif1, notif2);
        when(notificationRepository.findAllById(ids)).thenReturn(notifs);
        when(notificationRepository.saveAll(anyList())).thenReturn(notifs);

        notificationService.markNotificationsAsRead(ids);
        assertTrue(notif1.isRead());
        assertTrue(notif2.isRead());
        verify(notificationRepository, times(1)).saveAll(notifs);
    }

    @Test
    void testMarkAllAsReadByUser() {
        Notification notif1 = Notification.builder().id(1L).user(user).read(false).build();
        Notification notif2 = Notification.builder().id(2L).user(user).read(false).build();
        java.util.List<Notification> notifs = java.util.Arrays.asList(notif1, notif2);
        when(notificationRepository.findByUser_IdAndReadFalse(user.getId())).thenReturn(notifs);
        when(notificationRepository.saveAll(anyList())).thenReturn(notifs);

        notificationService.markAllAsReadByUser(user.getId());
        assertTrue(notif1.isRead());
        assertTrue(notif2.isRead());
        verify(notificationRepository, times(1)).saveAll(notifs);
    }

    @Test
    void testCountUnreadByUser() {
        when(notificationRepository.countByUser_IdAndReadFalse(user.getId())).thenReturn(3L);
        long count = notificationService.countUnreadByUser(user.getId());
        assertEquals(3L, count);
        verify(notificationRepository, times(1)).countByUser_IdAndReadFalse(user.getId());
    }
}
