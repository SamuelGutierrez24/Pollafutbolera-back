package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SendMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final String fromPhoneNumber;
    private final String fromWhatsAppNumber;

    public MessagingServiceImpl(
            @Value("${twilio.account.sid}") String accountSid,
            @Value("${twilio.auth.token}") String authToken,
            @Value("${twilio.phone.number}") String fromPhoneNumber,
            @Value("${twilio.whatsapp.phone.number}") String fromWhatsAppNumber
    ) {
        this.fromPhoneNumber = fromPhoneNumber;
        this.fromWhatsAppNumber = fromWhatsAppNumber;
        Twilio.init(accountSid, authToken);
    }

    @Override
    public ResponseEntity<String> sendSMS(SendMessageDto messageDto) {
        try {
            Message sms = Message.creator(
                    new PhoneNumber(messageDto.to()),
                    new PhoneNumber(fromPhoneNumber),
                    messageDto.message()
            ).create();
            return ResponseEntity.ok("SMS sent successfully: " + sms.getSid());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }

    // The sendWhatsApp method needs a Twilio account upgrade to send WhatsApp messages.
    @Override
    public ResponseEntity<String> sendWhatsApp(SendMessageDto messageDto) {
        try {
            String toWhatsAppNumber = "whatsapp:" + messageDto.to();
            String fromWhatsAppWithPrefix = "whatsapp:" + fromWhatsAppNumber;
            
            Message whatsAppMessage = Message.creator(
                    new PhoneNumber(toWhatsAppNumber),
                    new PhoneNumber(fromWhatsAppWithPrefix),
                    messageDto.message()
            ).create();
            
            return ResponseEntity.ok("WhatsApp message sent successfully: " + whatsAppMessage.getSid());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send WhatsApp message: " + e.getMessage());
        }
    }
}
