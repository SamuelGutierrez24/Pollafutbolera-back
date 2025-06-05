package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.EmailDTO;
import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import co.edu.icesi.pollafutbolera.exception.DuplicateEmailsException;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.EmailServiceImpl;
import co.edu.icesi.pollafutbolera.service.JwtService;
import co.edu.icesi.pollafutbolera.service.EncryptionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private JwtService jwtService;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private EncryptionService encripter;

    @InjectMocks
    private EmailServiceImpl emailService;

    private EmailPasswordDTO emailPasswordDTO;
    private User user;

    @BeforeEach
    void setUp() {
        emailPasswordDTO = new EmailPasswordDTO("test@example.com");
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        user = new User();
        user.setId(1L);
        user.setNickname("testuser");
        user.setPassword("encodedPassword");
        user.setName("Test User");
        user.setCedula("123456789");
        user.setMail("test@example.com");
        user.setRole(role);
    }

    @Test
    void sendEmailPassword_WithExistingUser_ShouldSendEmail() throws MessagingException {
        when(userRepository.findByMail(anyString())).thenReturn(Collections.singletonList(user));
        doReturn("test-token").when(jwtService).generateToken(user);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("changePassword"), any(Context.class))).thenReturn("<html>Test Content</html>");

        assertDoesNotThrow(() -> emailService.sendEmailPassword(emailPasswordDTO));

        verify(userRepository).findByMail("test@example.com");
        verify(jwtService).generateToken(user);
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(eq("changePassword"), any(Context.class));
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailPassword_WithNonExistingUser_ShouldNotSendEmail() throws MessagingException {
        when(userRepository.findByMail(anyString())).thenReturn(Collections.emptyList());

        emailService.sendEmailPassword(emailPasswordDTO);

        verify(userRepository).findByMail("test@example.com");
        verifyNoInteractions(jwtService);
        verifyNoInteractions(javaMailSender);
        verifyNoInteractions(templateEngine);
    }

    @Test
    void sendEmailPassword_WithMailException_ShouldThrowMessagingException() {
        when(userRepository.findByMail(anyString())).thenReturn(Collections.singletonList(user));
        doReturn("test-token").when(jwtService).generateToken(user);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("changePassword"), any(Context.class))).thenReturn("<html>Test Content</html>");

        doThrow(new RuntimeException("Mail sending failed"))
                .when(javaMailSender).send(any(MimeMessage.class));

        MessagingException exception = assertThrows(MessagingException.class, () ->
                emailService.sendEmailPassword(emailPasswordDTO)
        );

        assertTrue(exception.getMessage().contains("Error sending email"));

        verify(userRepository).findByMail("test@example.com");
        verify(jwtService).generateToken(user);
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(eq("changePassword"), any(Context.class));
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailPassword_ValidateTokenGeneration() throws MessagingException {
        String expectedToken = "valid-jwt-token";
        when(userRepository.findByMail(anyString())).thenReturn(Collections.singletonList(user));
        doReturn(expectedToken).when(jwtService).generateToken(user);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("changePassword"), any(Context.class))).thenReturn("<html>Test Content</html>");

        emailService.sendEmailPassword(emailPasswordDTO);

        verify(templateEngine).process(eq("changePassword"), argThat(context -> {
            String link = (String) context.getVariable("link");
            return link != null && link.contains(expectedToken);
        }));
    }

    @Test
    void sendNotificationEmail_sendsEmailSuccessfully() throws MessagingException {
        String to = "user@example.com";
        String subject = "Notificación";
        String content = "Este es el contenido de la notificación";
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email"), any(Context.class))).thenReturn("<html>Notificación</html>");

        assertDoesNotThrow(() -> emailService.sendNotificationEmail(to, subject, content));
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void sendEmailConfirmRegistration_ShouldSendEmailSuccessfully() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        String nickname = "testuser";
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("welcome"), any(Context.class))).thenReturn("<html>Welcome Content</html>");

        // Act & Assert
        assertDoesNotThrow(() -> emailService.sendEmailConfirmRegistration(email, nickname));

        // Verify
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(eq("welcome"), argThat(context -> {
            String nicknameVariable = (String) context.getVariable("nickname");
            return nicknameVariable != null && nicknameVariable.equals(nickname);
        }));
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void sendEmailConfirmRegistration_ShouldThrowMessagingException_WhenErrorOccurs() {
        // Arrange
        String email = "test@example.com";
        String nickname = "testuser";
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("welcome"), any(Context.class))).thenReturn("<html>Welcome Content</html>");
        doThrow(new RuntimeException("Mail sending failed")).when(javaMailSender).send(any(MimeMessage.class));

        // Act & Assert
        MessagingException exception = assertThrows(MessagingException.class, () ->
                emailService.sendEmailConfirmRegistration(email, nickname)
        );

        // Verify
        assertTrue(exception.getMessage().contains("Error sending email"));
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(eq("welcome"), any(Context.class));
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void send_ShouldSendMultipleEmailsSuccessfully() throws Exception {
        EmailDTO email1 = new EmailDTO("1", "user1@example.com", "Subject1", "User1","678910");
        EmailDTO email2 = new EmailDTO("2", "user2@example.com", "Subject2", "User2","123456");
        List<EmailDTO> emails = Arrays.asList(email1, email2);

        when(encripter.encrypt(anyString())).thenReturn("encryptedId");
        when(templateEngine.process(eq("invitacion"), any(Context.class))).thenReturn("<html>Email</html>");
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.send(emails));
        verify(javaMailSender, times(1)).send(any(MimeMessage[].class));
    }

    @Test
    void send_ShouldThrowDuplicateEmailsException() {
        EmailDTO email1 = new EmailDTO("1", "user@example.com", "User1", "Subject1","123456");
        EmailDTO email2 = new EmailDTO("2", "user@example.com", "User2", "Subject2","123456");
        List<EmailDTO> emails = Arrays.asList(email1, email2);

        assertThrows(DuplicateEmailsException.class, () -> emailService.send(emails));
        verify(javaMailSender, never()).send(any(MimeMessage[].class));
    }

    @Test
    void send_ShouldThrowRuntimeExceptionOnOtherErrors() throws Exception {
        EmailDTO email1 = new EmailDTO("1", "user@example.com", "User1", "Subject1","123456");
        List<EmailDTO> emails = List.of(email1);

        when(encripter.encrypt(anyString())).thenThrow(new RuntimeException("Encryption error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> emailService.send(emails));
        assertTrue(ex.getMessage().contains("Error to send email"));
        verify(javaMailSender, never()).send(any(MimeMessage[].class));
    }

}