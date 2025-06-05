package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.NotificationDTO;
import co.edu.icesi.pollafutbolera.model.Notification;
import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.NotificationRepository;
import co.edu.icesi.pollafutbolera.repository.TypeNotificationRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class NotificationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeNotificationRepository typeNotificationRepository;

    private User testUser;
    private TypeNotification testType;
    private Notification testNotification;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                String body = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
                System.err.println("Response error: " + body);
                super.handleError(response);
            }
        });

        // Configurar headers con tenant ID
        headers = new HttpHeaders();
        headers.add("X-TenantId", "1");
        headers.setContentType(MediaType.APPLICATION_JSON);

        notificationRepository.deleteAll();

        Optional<User> userOpt = userRepository.findById(10L);
        testUser = userOpt.orElseGet(() -> {
            User user = User.builder()
                    .name("Test User")
                    .mail("test@example.com")
                    .notificationsEmailEnabled(true)
                    .notificationsSMSEnabled(true)
                    .notificationsWhatsappEnabled(true)
                    .build();
            return userRepository.save(user);
        });

        TypeNotification existingType = typeNotificationRepository.findByName("TEST_NOTIFICATION");
        if (existingType != null) {
            testType = existingType;
        } else {
            testType = new TypeNotification("TEST_NOTIFICATION", "Test Notification");
            testType = typeNotificationRepository.save(testType);
        }

        testNotification = Notification.builder()
                .user(testUser)
                .content("Test notification content")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        testNotification = notificationRepository.save(testNotification);
    }

    @Test
    public void testGetUserNotifications() {
        String url = "http://localhost:" + port + "/pollafutbolera/notifications/user/" + testUser.getId() + "?page=0&size=10";

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PagedModel<NotificationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PagedModel<NotificationDTO>>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getContent().stream()
                .anyMatch(dto -> dto.getId().equals(testNotification.getId())));
    }

    @Test
    public void testMarkNotificationAsRead() {
        String url = "http://localhost:" + port + "/pollafutbolera/notifications/" + testNotification.getId() + "/read";

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Optional<Notification> updatedNotification = notificationRepository.findById(testNotification.getId());
        assertTrue(updatedNotification.isPresent());
        assertTrue(updatedNotification.get().isRead());
    }

    @Test
    public void testGetUnreadCount() {
        Notification notification1 = Notification.builder()
                .user(testUser)
                .content("Unread notification 1")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        Notification notification2 = Notification.builder()
                .user(testUser)
                .content("Unread notification 2")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        notificationRepository.saveAll(Arrays.asList(notification1, notification2));

        String url = "http://localhost:" + port + "/pollafutbolera/notifications/user/" + testUser.getId() + "/unread/count";

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Long.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() >= 3);
    }

    @Test
    public void testMarkAllAsRead() {
        Notification notification1 = Notification.builder()
                .user(testUser)
                .content("Unread notification 1")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        Notification notification2 = Notification.builder()
                .user(testUser)
                .content("Unread notification 2")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        notificationRepository.saveAll(Arrays.asList(notification1, notification2));

        String url = "http://localhost:" + port + "/pollafutbolera/notifications/user/" + testUser.getId() + "/read";

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        List<Notification> userNotifications = notificationRepository.findByUser_IdAndReadFalse(testUser.getId());
        assertTrue(userNotifications.isEmpty());
    }

    @Test
    public void testMarkBatchAsRead() {
        Notification notification1 = Notification.builder()
                .user(testUser)
                .content("Unread notification 1")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        Notification notification2 = Notification.builder()
                .user(testUser)
                .content("Unread notification 2")
                .timestamp(LocalDateTime.now())
                .type(testType)
                .read(false)
                .build();
        notification1 = notificationRepository.save(notification1);
        notification2 = notificationRepository.save(notification2);

        List<Long> notificationIds = Arrays.asList(notification1.getId(), notification2.getId());

        String url = "http://localhost:" + port + "/pollafutbolera/notifications/user/" + testUser.getId() + "/read/batch";

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(notificationIds, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Optional<Notification> updatedNotification1 = notificationRepository.findById(notification1.getId());
        Optional<Notification> updatedNotification2 = notificationRepository.findById(notification2.getId());
        Optional<Notification> originalNotification = notificationRepository.findById(testNotification.getId());

        assertTrue(updatedNotification1.isPresent() && updatedNotification1.get().isRead());
        assertTrue(updatedNotification2.isPresent() && updatedNotification2.get().isRead());
        assertTrue(originalNotification.isPresent() && !originalNotification.get().isRead());
    }
}
