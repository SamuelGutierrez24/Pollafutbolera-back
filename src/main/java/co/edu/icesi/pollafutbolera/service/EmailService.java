package co.edu.icesi.pollafutbolera.service;

import java.util.List;

import co.edu.icesi.pollafutbolera.dto.EmailDTO;
import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import jakarta.mail.MessagingException;

public interface EmailService {

	String send(EmailDTO email) throws MessagingException;
	String send(List<EmailDTO> emails) throws MessagingException;
    void sendEmailPassword(EmailPasswordDTO emailPasswordDTO) throws MessagingException;
    void sendEmailConfirmRegistration(String email, String token) throws MessagingException;
    void sendNotificationEmail(String to, String subject, String content) throws MessagingException;
}
