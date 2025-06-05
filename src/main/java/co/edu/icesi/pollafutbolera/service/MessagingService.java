package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SendMessageDto;
import org.springframework.http.ResponseEntity;

public interface MessagingService {
    ResponseEntity<String> sendSMS(SendMessageDto messageDto);
    ResponseEntity<String> sendWhatsApp(SendMessageDto messageDto);
}
