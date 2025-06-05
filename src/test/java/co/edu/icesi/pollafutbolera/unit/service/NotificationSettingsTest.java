package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.NotificationSettingsDTO;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationSettingsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        Role role = Role.builder().id(1L).name("USER").build();
        
        testUser = User.builder()
                .id(userId)
                .name("Test User")
                .lastName("Test Last Name")
                .cedula("1234567890")
                .nickname("testuser")
                .mail("test@example.com")
                .password("Password123!")
                .phoneNumber("1234567890")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .role(role)
                .extraInfo("{}")
                .build();
    }

    @Test
    void testGetNotificationSettings() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        ResponseEntity<NotificationSettingsDTO> response = userService.getNotificationSettings(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().userId());
        assertEquals(true, response.getBody().enabledEmail());
        assertEquals(true, response.getBody().enabledSMS());
        assertEquals(true, response.getBody().enabledWhatsapp());
        
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetNotificationSettings_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<NotificationSettingsDTO> response = userService.getNotificationSettings(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateNotificationSettings_Enable() {
        NotificationSettingsDTO settings = NotificationSettingsDTO.builder()
                .userId(userId)
                .enabledEmail(true)
                .enabledSMS(true)
                .enabledWhatsapp(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<NotificationSettingsDTO> response = userService.updateNotificationSettings(userId, settings);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().userId());
        assertEquals(true, response.getBody().enabledEmail());
        assertEquals(true, response.getBody().enabledSMS());
        assertEquals(true, response.getBody().enabledWhatsapp());
        assertTrue(testUser.isNotificationsEmailEnabled());
        assertTrue(testUser.isNotificationsSMSEnabled());
        assertTrue(testUser.isNotificationsWhatsappEnabled());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateNotificationSettings_Disable() {
        NotificationSettingsDTO settings = NotificationSettingsDTO.builder()
                .userId(userId)
                .enabledEmail(false)
                .enabledSMS(false)
                .enabledWhatsapp(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<NotificationSettingsDTO> response = userService.updateNotificationSettings(userId, settings);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().userId());
        assertEquals(false, response.getBody().enabledEmail());
        assertEquals(false, response.getBody().enabledSMS());
        assertEquals(false, response.getBody().enabledWhatsapp());
        assertFalse(testUser.isNotificationsEmailEnabled());
        assertFalse(testUser.isNotificationsSMSEnabled());
        assertFalse(testUser.isNotificationsWhatsappEnabled());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateNotificationSettings_UserNotFound() {
        NotificationSettingsDTO settings = NotificationSettingsDTO.builder()
                .userId(userId)
                .enabledEmail(true)
                .enabledSMS(true)
                .enabledWhatsapp(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<NotificationSettingsDTO> response = userService.updateNotificationSettings(userId, settings);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }
} 