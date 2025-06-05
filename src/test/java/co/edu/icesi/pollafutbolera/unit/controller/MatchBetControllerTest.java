package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.MatchBetController;
import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.service.MatchBetService;
import co.edu.icesi.pollafutbolera.util.MatchBetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class MatchBetControllerTest {

    @Mock
    private MatchBetService matchBetService;

    @InjectMocks
    private MatchBetController matchBetController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMatchBet() {
        Long matchBetId = 1L;
        MatchBetResponseDTO matchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();

        when(matchBetService.getMatchBetById(matchBetId)).thenReturn(ResponseEntity.ok(matchBetResponseDTO));

        ResponseEntity<MatchBetResponseDTO> response = matchBetController.getMatchBet(matchBetId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(matchBetResponseDTO), response);
        verify(matchBetService, times(1)).getMatchBetById(matchBetId);
    }

    @Test
    public void testCreateMatchBet() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();
        MatchBetResponseDTO matchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();

        when(matchBetService.createMatchBet(matchBetDTO)).thenReturn(ResponseEntity.status(201).body(matchBetResponseDTO));

        ResponseEntity<MatchBetResponseDTO> response = matchBetController.createMatchBet(matchBetDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.status(201).body(matchBetResponseDTO), response);
        verify(matchBetService, times(1)).createMatchBet(matchBetDTO);
    }

    @Test
    public void testUpdateMatchBet() {
        Long matchBetId = 1L;
        MatchBetUpdateDTO matchBetUpdateDTO = MatchBetUtil.matchBetUpdateDTO();
        MatchBetResponseDTO matchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();

        when(matchBetService.updateMatchBet(matchBetId, matchBetUpdateDTO)).thenReturn(ResponseEntity.ok(matchBetResponseDTO));

        ResponseEntity<MatchBetResponseDTO> response = matchBetController.updateMatchBet(matchBetId, matchBetUpdateDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(matchBetResponseDTO), response);
        verify(matchBetService, times(1)).updateMatchBet(matchBetId, matchBetUpdateDTO);
    }

    @Test
    public void testDeleteMatchBet() {
        Long matchBetId = 1L;

        when(matchBetService.deleteMatchBet(matchBetId)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = matchBetController.deleteMatchBet(matchBetId);

        assertNotNull(response);
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(matchBetService, times(1)).deleteMatchBet(matchBetId);
    }

    @Test
    public void testGetMatchBetsByUser() {
        Long userId = 1L;
        List<MatchBetResponseDTO> matchBetResponseDTOs = List.of(MatchBetUtil.matchBetResponseDTO());

        when(matchBetService.getMatchBetsByUser(userId)).thenReturn(ResponseEntity.ok(matchBetResponseDTOs));

        ResponseEntity<List<MatchBetResponseDTO>> response = matchBetController.getMatchBetsByUser(userId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(matchBetResponseDTOs), response);
        verify(matchBetService, times(1)).getMatchBetsByUser(userId);
    }

    @Test
    public void testGetMatchBetsByUserAndPolla() {
        Long userId = 1L;
        Long pollaId = 1L;
        List<MatchBetResponseDTO> matchBetResponseDTOs = List.of(MatchBetUtil.matchBetResponseDTO());

        when(matchBetService.getMatchBetsByUserAndPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(matchBetResponseDTOs));

        ResponseEntity<List<MatchBetResponseDTO>> response = matchBetController.getMatchBetsByUserAndPolla(userId, pollaId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(matchBetResponseDTOs), response);
        verify(matchBetService, times(1)).getMatchBetsByUserAndPolla(userId, pollaId);
    }
}
