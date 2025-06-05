package co.edu.icesi.pollafutbolera.scheduled;

import co.edu.icesi.pollafutbolera.dto.SendMessageDto;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import co.edu.icesi.pollafutbolera.service.EmailService;
import co.edu.icesi.pollafutbolera.service.MessagingService;
import co.edu.icesi.pollafutbolera.service.NotificationService;
import co.edu.icesi.pollafutbolera.service.TypeNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Servicio para el envío automático de notificaciones por SMS, WhatsApp e Intra App")
public class ScheduledMessaging {

    private final MessagingService messagingService;
    private final UserRepository userRepository;
    private final PollaRepository pollaRepository;
    private final UserScoresPollaRepository userScoresPollaRepository;
    private final NotificationService notificationService;
    private final TypeNotificationService typeNotificationService;
    private final EmailService emailService;

    private static final String DAILY_NOTIFICATION_CRON = "0 36 8 * * ?";
    private static final String PREDICTION_REMINDER_CRON = "0 0 20 * * ?";
    private static final String SCORE_UPDATE_CRON = "0 0 10 * * ?";

    @Scheduled(cron = DAILY_NOTIFICATION_CRON)
    @Operation(summary = "Envía recordatorios diarios por SMS, WhatsApp, Email e Intra App a los usuarios")
    public void sendDailyNotifications() {
        List<User> users = userRepository.findAll();
        TypeNotification type = typeNotificationService.getOrCreate("DAILY_REMINDER", "Recordatorio diario de predicciones");
        for (User user : users) {
            String content = "¡Hola " + user.getName() + "! No olvides revisar la polla y hacer tus predicciones para hoy. Consulta tus estadísticas y posición en el ranking.";
            String subject = "Recordatorio diario: ¡Haz tus predicciones y revisa tu polla!";
            notificationService.createNotification(user, content, type);
            sendExternalNotifications(user, content, subject);
        }
    }

    @Scheduled(cron = PREDICTION_REMINDER_CRON)
    @Operation(summary = "Envía recordatorios para hacer predicciones antes del cierre por SMS, WhatsApp, Email e Intra App")
    public void sendPredictionReminders() {
        List<User> users = userRepository.findAll();
        List<Polla> activePollas = pollaRepository.findAll().stream()
                .filter(polla -> polla.getDeletedAt() == null && isActiveDate(polla))
                .toList();
        TypeNotification type = typeNotificationService.getOrCreate("PREDICTION_REMINDER", "Recordatorio de predicción antes del cierre");
        for (User user : users) {
            for (Polla polla : activePollas) {
                boolean hasPrediction = userScoresPollaRepository.findByUser_IdAndPolla_Id(user.getId(), polla.getId()).isPresent();
                if (!hasPrediction) {
                    String content = "¡Recordatorio! Tienes hasta hoy para hacer tus predicciones en la polla '" +
                            polla.getTournament().getName() + "'. ¡No pierdas puntos!";
                    String subject = "Recordatorio: Predicciones pendientes en la polla '" + polla.getTournament().getName() + "'";
                    notificationService.createNotification(user, content, type);
                    sendExternalNotifications(user, content, subject);
                }
            }
        }
    }

    @Scheduled(cron = SCORE_UPDATE_CRON)
    @Operation(summary = "Envía notificaciones de actualización de puntajes después de jornadas por SMS, WhatsApp, Email e Intra App")
    public void sendScoreUpdates() {
        List<User> users = userRepository.findAll();
        TypeNotification type = typeNotificationService.getOrCreate("SCORE_UPDATE", "Actualización de puntaje tras jornada");
        for (User user : users) {
            List<Polla> activePollas = pollaRepository.findAll().stream()
                    .filter(polla -> polla.getDeletedAt() == null && isActiveDate(polla))
                    .toList();
            for (Polla polla : activePollas) {
                userScoresPollaRepository.findByUser_IdAndPolla_Id(user.getId(), polla.getId())
                        .ifPresent(userScoresPolla -> {
                            int score = userScoresPolla.getScores();
                            String content = "¡Actualización de puntaje en " + polla.getTournament().getName() +
                                    "! Tu puntaje actual es: " + score + ". ¡Sigue participando!";
                            String subject = "Actualización de tu puntaje en la polla '" + polla.getTournament().getName() + "'";
                            notificationService.createNotification(user, content, type);
                            sendExternalNotifications(user, content, subject);
                        });
            }
        }
    }

    private void sendExternalNotifications(User user, String content, String subject) {
        //if (isValidNotificationTime()) {
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                SendMessageDto message = SendMessageDto.builder()
                        .to(user.getPhoneNumber())
                        .message(content)
                        .build();
                if(user.isNotificationsSMSEnabled()){
                    messagingService.sendSMS(message);
                }
                if(user.isNotificationsWhatsappEnabled()){
                    messagingService.sendWhatsApp(message);
                }
            }
            if (user.getMail() != null && !user.getMail().isEmpty()) {
                try {
                    if(user.isNotificationsEmailEnabled()){
                        emailService.sendNotificationEmail(user.getMail(), subject, content);
                    }
                } catch (jakarta.mail.MessagingException e) {
                    System.err.println("Error sending email: " + e.getMessage());
                }
            }
        //}
    }

    public boolean isValidNotificationTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(21, 0));
    }

    public boolean isActiveDate(Polla polla) {
        Date now = new Date();
        return polla.getStartDate().before(now) && polla.getEndDate().after(now);
    }
}
