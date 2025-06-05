package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.EmailDTO;

import java.util.List;
import java.util.Map;
import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/email")
public interface EmailAPI {
    @PostMapping()
    @PreAuthorize("permitAll()")
    ResponseEntity<String> sendEmail(@RequestBody EmailDTO email) throws MessagingException;
    
    @PostMapping("/sendToAll")
    @PreAuthorize("permitAll()")
    ResponseEntity<String> sendEmails(@RequestBody List<EmailDTO> emails) throws MessagingException;
    
    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    ResponseEntity<Map<String, String>> sendEmailPassword(@RequestBody EmailPasswordDTO email) throws MessagingException;
}
