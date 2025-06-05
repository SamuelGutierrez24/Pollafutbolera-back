package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.repository.TypeNotificationRepository;
import co.edu.icesi.pollafutbolera.service.TypeNotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TypeNotificationServiceTest {
    @Mock
    private TypeNotificationRepository typeNotificationRepository;

    @InjectMocks
    private TypeNotificationServiceImpl typeNotificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrCreate_Existing() {
        String name = "EXISTING";
        String description = "desc";
        TypeNotification type = new TypeNotification(name, description);
        when(typeNotificationRepository.findByName(name)).thenReturn(type);

        TypeNotification result = typeNotificationService.getOrCreate(name, description);
        assertEquals(type, result);
        verify(typeNotificationRepository, never()).save(any());
    }

    @Test
    void testGetOrCreate_New() {
        String name = "NEW";
        String description = "desc";
        when(typeNotificationRepository.findByName(name)).thenReturn(null);
        TypeNotification saved = new TypeNotification(name, description);
        when(typeNotificationRepository.save(any(TypeNotification.class))).thenReturn(saved);

        TypeNotification result = typeNotificationService.getOrCreate(name, description);
        assertEquals(saved, result);
        verify(typeNotificationRepository, times(1)).save(any(TypeNotification.class));
    }
}
