package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.SendMessageDto;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import co.edu.icesi.pollafutbolera.service.EmailService;
import co.edu.icesi.pollafutbolera.service.MessagingService;
import co.edu.icesi.pollafutbolera.service.NotificationService;
import co.edu.icesi.pollafutbolera.service.TypeNotificationService;
import co.edu.icesi.pollafutbolera.scheduled.ScheduledMessaging;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledMessagingTest {

    @Mock
    private MessagingService messagingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PollaRepository pollaRepository;
    @Mock
    private UserScoresPollaRepository userScoresPollaRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private TypeNotificationService typeNotificationService;
    @Mock
    private EmailService emailService;

    @Spy
    @InjectMocks
    private ScheduledMessaging scheduledMessaging;

    private User testUser;
    private Polla testPolla;
    private UserScoresPolla testUserScoresPolla;
    private TypeNotification dailyType;
    private TypeNotification predType;
    private TypeNotification scoreType;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .mail("test@example.com")
                .phoneNumber("+573001234567")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();
                
        Tournament testTournament = Tournament.builder()
                .id(1L)
                .name("Test Tournament")
                .build();
                
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 7);
        
        testPolla = Polla.builder()
                .id(1L)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .tournament(testTournament)
                .build();
                
        testUserScoresPolla = UserScoresPolla.builder()
                .user(testUser)
                .polla(testPolla)
                .scores(100)
                .build();
                
        dailyType = new TypeNotification("DAILY_REMINDER", "Recordatorio diario de predicciones");
        predType = new TypeNotification("PREDICTION_REMINDER", "Recordatorio de predicción antes del cierre");
        scoreType = new TypeNotification("SCORE_UPDATE", "Actualización de puntaje tras jornada");

        lenient().when(notificationService.createNotification(any(), any(), any())).thenReturn(new Notification());
        lenient().when(messagingService.sendSMS(any(SendMessageDto.class))).thenReturn(ResponseEntity.ok("SMS sent"));
        lenient().when(messagingService.sendWhatsApp(any(SendMessageDto.class))).thenReturn(ResponseEntity.ok("WhatsApp sent"));
    }


    @Test
    void testSendDailyNotifications_createsNotificationsForAllUsers() {
        List<User> users = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(notificationService).createNotification(eq(testUser), anyString(), eq(dailyType));
    }
    
    @Test
    void testSendDailyNotifications_sendsSMSAndWhatsAppIfUserHasPhone() {
        List<User> users = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(messagingService).sendSMS(any(SendMessageDto.class));
        verify(messagingService).sendWhatsApp(any(SendMessageDto.class));
    }
    
    @Test
    void testSendDailyNotifications_sendsEmailIfUserHasEmail() throws MessagingException {
        List<User> users = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(emailService).sendNotificationEmail(eq("test@example.com"), anyString(), anyString());
    }
    
    @Test
    void testSendDailyNotifications_doesNotSendSMSIfUserHasNoPhone() {
        User userWithoutPhone = User.builder()
                .id(1L)
                .name("Test User")
                .mail("test@example.com")
                .phoneNumber(null)
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();
                
        List<User> users = Collections.singletonList(userWithoutPhone);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(notificationService).createNotification(eq(userWithoutPhone), anyString(), eq(dailyType));
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService, never()).sendWhatsApp(any(SendMessageDto.class));
    }
    
    @Test
    void testSendDailyNotifications_doesNotSendEmailIfUserHasNoEmail() throws MessagingException {
        User userWithoutEmail = User.builder()
                .id(1L)
                .name("Test User")
                .mail(null)
                .phoneNumber("+573001234567")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();
                
        List<User> users = Collections.singletonList(userWithoutEmail);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(notificationService).createNotification(eq(userWithoutEmail), anyString(), eq(dailyType));
        verify(emailService, never()).sendNotificationEmail(anyString(), anyString(), anyString());
    }
    
    @Test
    void testSendDailyNotifications_doesNotSendSMSIfNotificationsDisabled() {
        User userWithSMSDisabled = User.builder()
                .id(1L)
                .name("Test User")
                .mail("test@example.com")
                .phoneNumber("+573001234567")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(false)
                .notificationsWhatsappEnabled(true)
                .build();
                
        List<User> users = Collections.singletonList(userWithSMSDisabled);
        when(userRepository.findAll()).thenReturn(users);
        when(typeNotificationService.getOrCreate(eq("DAILY_REMINDER"), anyString())).thenReturn(dailyType);

        scheduledMessaging.sendDailyNotifications();

        verify(notificationService).createNotification(eq(userWithSMSDisabled), anyString(), eq(dailyType));
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService).sendWhatsApp(any(SendMessageDto.class));
    }

    @Test
    void testSendPredictionReminders_sendsNotificationsToUsersWithoutPredictions() {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        when(userRepository.findAll()).thenReturn(users);
        when(pollaRepository.findAll()).thenReturn(pollas);
        when(typeNotificationService.getOrCreate(eq("PREDICTION_REMINDER"), anyString())).thenReturn(predType);

        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        doReturn(true).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendPredictionReminders();

        verify(notificationService).createNotification(eq(testUser), anyString(), eq(predType));
        verify(messagingService).sendSMS(any(SendMessageDto.class));
        verify(messagingService).sendWhatsApp(any(SendMessageDto.class));
    }
    
    @Test
    void testSendPredictionReminders_doesNotSendNotificationsToUsersWithPredictions() {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        when(userRepository.findAll()).thenReturn(users);
        when(pollaRepository.findAll()).thenReturn(pollas);
        when(typeNotificationService.getOrCreate(eq("PREDICTION_REMINDER"), anyString())).thenReturn(predType);
        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.of(testUserScoresPolla));
        doReturn(true).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendPredictionReminders();

        verify(notificationService, never()).createNotification(any(), any(), any());
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService, never()).sendWhatsApp(any(SendMessageDto.class));
    }
    
    @Test
    void testSendPredictionReminders_doesNotSendNotificationsForInactivePollas() {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        lenient().when(userRepository.findAll()).thenReturn(users);
        lenient().when(pollaRepository.findAll()).thenReturn(pollas);
        lenient().when(typeNotificationService.getOrCreate(eq("PREDICTION_REMINDER"), anyString())).thenReturn(predType);
        lenient().when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.empty());
        doReturn(false).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendPredictionReminders();

        verify(notificationService, never()).createNotification(any(), any(), any());
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService, never()).sendWhatsApp(any(SendMessageDto.class));
    }

    
    @Test
    void testSendScoreUpdates_sendsNotificationsToUsersWithScores() {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        when(userRepository.findAll()).thenReturn(users);
        when(pollaRepository.findAll()).thenReturn(pollas);
        when(typeNotificationService.getOrCreate(eq("SCORE_UPDATE"), anyString())).thenReturn(scoreType);
        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.of(testUserScoresPolla));
        doReturn(true).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendScoreUpdates();

        verify(notificationService).createNotification(eq(testUser), anyString(), eq(scoreType));
        verify(messagingService).sendSMS(any(SendMessageDto.class));
        verify(messagingService).sendWhatsApp(any(SendMessageDto.class));
    }
    
    @Test
    void testSendScoreUpdates_sendsEmailWithScore() throws MessagingException {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        when(userRepository.findAll()).thenReturn(users);
        when(pollaRepository.findAll()).thenReturn(pollas);
        when(typeNotificationService.getOrCreate(eq("SCORE_UPDATE"), anyString())).thenReturn(scoreType);
        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.of(testUserScoresPolla));
        doReturn(true).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendScoreUpdates();

        verify(notificationService).createNotification(eq(testUser), anyString(), eq(scoreType));
        verify(emailService).sendNotificationEmail(eq("test@example.com"), anyString(), anyString());
    }
    
    @Test
    void testSendScoreUpdates_doesNotSendNotificationsForUsersWithoutScores() throws MessagingException {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        when(userRepository.findAll()).thenReturn(users);
        when(pollaRepository.findAll()).thenReturn(pollas);
        when(typeNotificationService.getOrCreate(eq("SCORE_UPDATE"), anyString())).thenReturn(scoreType);
        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.empty());
        doReturn(true).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendScoreUpdates();

        verify(notificationService, never()).createNotification(any(), any(), any());
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService, never()).sendWhatsApp(any(SendMessageDto.class));
        verify(emailService, never()).sendNotificationEmail(anyString(), anyString(), anyString());
    }
    
    @Test
    void testSendScoreUpdates_doesNotSendNotificationsForInactivePollas() throws MessagingException {
        List<User> users = Collections.singletonList(testUser);
        List<Polla> pollas = Collections.singletonList(testPolla);
        
        lenient().when(userRepository.findAll()).thenReturn(users);
        lenient().when(pollaRepository.findAll()).thenReturn(pollas);
        lenient().when(typeNotificationService.getOrCreate(eq("SCORE_UPDATE"), anyString())).thenReturn(scoreType);
        lenient().when(userScoresPollaRepository.findByUser_IdAndPolla_Id(anyLong(), anyLong())).thenReturn(Optional.of(testUserScoresPolla));
        doReturn(false).when(scheduledMessaging).isActiveDate(any(Polla.class));

        scheduledMessaging.sendScoreUpdates();

        verify(notificationService, never()).createNotification(any(), any(), any());
        verify(messagingService, never()).sendSMS(any(SendMessageDto.class));
        verify(messagingService, never()).sendWhatsApp(any(SendMessageDto.class));
        verify(emailService, never()).sendNotificationEmail(anyString(), anyString(), anyString());
    }
}