package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.SendMessageDto;
import co.edu.icesi.pollafutbolera.service.MessagingService;
import co.edu.icesi.pollafutbolera.service.MessagingServiceImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class MessagingServiceTest {

    private MessagingService messagingService;

    @BeforeEach
    void setUp() {
        messagingService = new MessagingServiceImpl(
                "test_sid", 
                "test_token", 
                "+1234567890", 
                "+1234567890"
        );
    }

    @Test
    void testSendSMS_Success() {
        SendMessageDto dto = new SendMessageDto("+573012757801", "Test Message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);
            Message messageMock = mock(Message.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class)))
                    .thenReturn(messageCreatorMock);

            Mockito.when(messageCreatorMock.create()).thenReturn(messageMock);
            Mockito.when(messageMock.getSid()).thenReturn("SM123456789");

            ResponseEntity<String> response = messagingService.sendSMS(dto);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("SMS sent successfully: SM123456789", response.getBody());
        }
    }

    @Test
    void testSendSMS_Failure() {
        SendMessageDto dto = new SendMessageDto("+573001787888", "Test Message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class)))
                    .thenReturn(messageCreatorMock);

            Mockito.when(messageCreatorMock.create()).thenThrow(new RuntimeException("Twilio error"));

            ResponseEntity<String> response = messagingService.sendSMS(dto);

            assertEquals(500, response.getStatusCode().value());
            assertEquals("Failed to send SMS: Twilio error", response.getBody());
        }
    }
    
    @Test
    void testSendWhatsApp_Success() {
        SendMessageDto dto = new SendMessageDto("+573012757801", "Test WhatsApp Message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);
            Message messageMock = mock(Message.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class)))
                    .thenReturn(messageCreatorMock);

            Mockito.when(messageCreatorMock.create()).thenReturn(messageMock);
            Mockito.when(messageMock.getSid()).thenReturn("WA123456789");

            ResponseEntity<String> response = messagingService.sendWhatsApp(dto);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("WhatsApp message sent successfully: WA123456789", response.getBody());
        }
    }

    @Test
    void testSendWhatsApp_Failure() {
        SendMessageDto dto = new SendMessageDto("+573001787888", "Test WhatsApp Message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class)))
                    .thenReturn(messageCreatorMock);

            Mockito.when(messageCreatorMock.create()).thenThrow(new RuntimeException("Twilio WhatsApp error"));

            ResponseEntity<String> response = messagingService.sendWhatsApp(dto);

            assertEquals(500, response.getStatusCode().value());
            assertEquals("Failed to send WhatsApp message: Twilio WhatsApp error", response.getBody());
        }
    }
}
