package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.mapper.SubPollaJoinRequestMapper;
import co.edu.icesi.pollafutbolera.repository.*;
import co.edu.icesi.pollafutbolera.service.SubPollaJoinRequestServiceImpl;
import co.edu.icesi.pollafutbolera.util.SubPollaJoinRequestUtil;
import co.edu.icesi.pollafutbolera.service.NotificationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SubPollaJoinRequestServiceTest {

    @Mock
    private SubPollaJoinRequestRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubPollaRepository subpollaRepository;

    @Mock
    private SubPollaJoinRequestMapper mapper;

    @Mock
    private UserSubPollaRepository userSubPollaRepository;

    @Mock
    private TypeNotificationRepository typeNotificationRepository;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private SubPollaJoinRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    // --------- createJoinRequest tests ---------

    @Test
    void testCreateJoinRequest_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.user()));
        when(subpollaRepository.findById(10L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin())));
        when(repository.save(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin())));
        when(mapper.toDTO(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestGetDTO());
        when(typeNotificationRepository.findByName("JOIN_REQUEST")).thenReturn(SubPollaJoinRequestUtil.typeNotification());

        ResponseEntity<SubPollaJoinRequestGetDTO> response = service.createJoinRequest(SubPollaJoinRequestUtil.createDTO());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PENDING", response.getBody().status().name());
    }

    @Test
    void testCreateJoinRequest_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.createJoinRequest(SubPollaJoinRequestUtil.createDTO()));
    }

    @Test
    void testCreateJoinRequest_subpollaNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.user()));
        when(subpollaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.createJoinRequest(SubPollaJoinRequestUtil.createDTO()));
    }

    // --------- respondToJoinRequest tests ---------

    @Test
    void testRespondToJoinRequest_accept_success() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("adminuser", null)
        );

        when(repository.findById(100L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin()))));
        when(userRepository.findByNickname("adminuser")).thenReturn(Optional.of(SubPollaJoinRequestUtil.admin()));
        when(userSubPollaRepository.save(any())).thenReturn(SubPollaJoinRequestUtil.userSubPolla(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin())));
        when(repository.save(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestAccepted(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin())));
        when(mapper.toDTO(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestGetDTO());
        when(typeNotificationRepository.findByName("JOIN_REQUEST")).thenReturn(SubPollaJoinRequestUtil.typeNotification());


        ResponseEntity<SubPollaJoinRequestGetDTO> response = service.respondToJoinRequest(100L, SubPollaJoinRequestUtil.acceptResponse());

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRespondToJoinRequest_reject_success() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("adminuser", null)
        );

        when(repository.findById(100L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin()))));
        when(userRepository.findByNickname("adminuser")).thenReturn(Optional.of(SubPollaJoinRequestUtil.admin()));
        when(repository.save(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin())));
        when(mapper.toDTO(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestGetDTO());

        ResponseEntity<SubPollaJoinRequestGetDTO> response = service.respondToJoinRequest(100L, SubPollaJoinRequestUtil.rejectResponse());

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRespondToJoinRequest_notFound() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.respondToJoinRequest(100L, SubPollaJoinRequestUtil.acceptResponse()));
    }

    @Test
    void testRespondToJoinRequest_notAdmin() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("adminuser", null)
        );

        when(repository.findById(100L)).thenReturn(Optional.of(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.user()))));
        when(userRepository.findByNickname("adminuser")).thenReturn(Optional.of(SubPollaJoinRequestUtil.admin()));

        assertThrows(SecurityException.class, () -> service.respondToJoinRequest(100L, SubPollaJoinRequestUtil.acceptResponse()));
    }

    // --------- getRequestsBySubpolla tests ---------

    @Test
    void testGetRequestsBySubpolla_success() {
        when(repository.findBySubpollaId(10L)).thenReturn(List.of(SubPollaJoinRequestUtil.joinRequestPending(SubPollaJoinRequestUtil.user(), SubPollaJoinRequestUtil.subpolla(SubPollaJoinRequestUtil.admin()))));
        when(mapper.toDTO(any())).thenReturn(SubPollaJoinRequestUtil.joinRequestGetDTO());

        ResponseEntity<List<SubPollaJoinRequestGetDTO>> response = service.getRequestsBySubpolla(10L);

        assertEquals(1, response.getBody().size());
        assertEquals(200, response.getStatusCodeValue());
    }
}
