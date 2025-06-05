package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.EmailController;
import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import co.edu.icesi.pollafutbolera.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTests {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private EmailPasswordDTO validEmail;
    private EmailPasswordDTO invalidEmail;
    private EmailPasswordDTO nullEmail;

    @BeforeEach
    void setUp() {
        validEmail = new EmailPasswordDTO("test@example.com");

        invalidEmail = new EmailPasswordDTO("invalid-email");

        nullEmail = new EmailPasswordDTO(null);

    }

    @Test
    void sendEmailPassword_WithValidEmail_ShouldReturnOk() throws MessagingException {
        doNothing().when(emailService).sendEmailPassword(validEmail);

        ResponseEntity<Map<String, String>> response = emailController.sendEmailPassword(validEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Correo de recuperación enviado correctamente.", response.getBody().get("message"));
        verify(emailService, times(1)).sendEmailPassword(validEmail);
    }

    @Test
    void sendEmailPassword_WithInvalidEmail_ShouldReturnBadRequest() throws MessagingException {
        ResponseEntity<Map<String, String>> response = emailController.sendEmailPassword(invalidEmail);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Correo electrónico no válido", response.getBody().get("error"));
        verify(emailService, never()).sendEmailPassword(any());
    }

    @Test
    void sendEmailPassword_WithNullEmail_ShouldReturnBadRequest() throws MessagingException {
        ResponseEntity<Map<String, String>> response = emailController.sendEmailPassword(nullEmail);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Correo electrónico no válido", response.getBody().get("error"));
        verify(emailService, never()).sendEmailPassword(any());
    }

    @Test
    void sendEmailPassword_WhenMessagingExceptionThrown_ShouldReturnInternalServerError() throws MessagingException {
        doThrow(new MessagingException("Error sending email")).when(emailService).sendEmailPassword(validEmail);

        ResponseEntity<Map<String, String>> response = emailController.sendEmailPassword(validEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("Error al enviar el correo"));
        verify(emailService, times(1)).sendEmailPassword(validEmail);
    }

    @Test
    void sendEmailPassword_WhenGenericExceptionThrown_ShouldReturnInternalServerError() throws MessagingException {
        doThrow(new RuntimeException("Unexpected error")).when(emailService).sendEmailPassword(validEmail);

        ResponseEntity<Map<String, String>> response = emailController.sendEmailPassword(validEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("Error interno del servidor"));
        verify(emailService, times(1)).sendEmailPassword(validEmail);
    }
}