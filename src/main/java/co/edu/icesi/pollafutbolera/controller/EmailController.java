package co.edu.icesi.pollafutbolera.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import co.edu.icesi.pollafutbolera.api.EmailAPI;
import co.edu.icesi.pollafutbolera.dto.EmailDTO;
import co.edu.icesi.pollafutbolera.service.EmailService;
import jakarta.mail.MessagingException;

import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Tag(name = "Email", description = "This API allows you all to manage the Email Logic")
public class EmailController implements EmailAPI {

    @Autowired
    EmailService emailService;

    @Override
	public ResponseEntity<String> sendEmail(EmailDTO email) throws MessagingException {
		return ResponseEntity.ok(emailService.send(email));
	}

	@Override
	@Operation(summary = "Send Email a link to sign up",
    description = "Sends an email containing the link to sign up",
    responses = {
            @ApiResponse(responseCode = "200", description = "Email(s) sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email address or duplicate emails"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })   
	public ResponseEntity<String> sendEmails(@RequestBody List<EmailDTO> emails)  {
		try {
			emailService.send(emails);
			return ResponseEntity.ok("Emails sent successfully");
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to send emails: " + e.getMessage());
		}
	}

    @Override
    @Operation(summary = "Send Email a link to change the password",
            description = "Sends an email containing the link to change the password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid email address"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })    
    public ResponseEntity<Map<String, String>> sendEmailPassword(@RequestBody EmailPasswordDTO email) {
        Map<String, String> response = new HashMap<>();

        if (email.addressee() == null || !email.addressee().contains("@")) {
            response.put("error", "Correo electrónico no válido");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            emailService.sendEmailPassword(email);
            response.put("message", "Correo de recuperación enviado correctamente.");
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put("error", "Error al enviar el correo: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}


