package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.TournamentBetController;
import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.service.TournamentBetService;
import co.edu.icesi.pollafutbolera.util.TournamentBetUtil;
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
public class TournamentBetControllerTest {

    @Mock
    private TournamentBetService tournamentBetService;

    @InjectMocks
    private TournamentBetController tournamentBetController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTournamentBet() {
        Long tournamentBetId = 1L;
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetService.getTournamentBetById(tournamentBetId)).thenReturn(ResponseEntity.ok(tournamentBetDTO));

        ResponseEntity<TournamentBetDTO> response = tournamentBetController.getTournamentBet(tournamentBetId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(tournamentBetDTO), response);
        verify(tournamentBetService, times(1)).getTournamentBetById(tournamentBetId);
    }

    @Test
    public void testCreateTournamentBet() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetService.createTournamentBet(tournamentBetDTO)).thenReturn(ResponseEntity.status(201).body(tournamentBetDTO));

        ResponseEntity<TournamentBetDTO> response = tournamentBetController.createTournamentBet(tournamentBetDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.status(201).body(tournamentBetDTO), response);
        verify(tournamentBetService, times(1)).createTournamentBet(tournamentBetDTO);
    }

    @Test
    public void testUpdateTournamentBet() {
        Long tournamentBetId = 1L;
        TournamentBetUpdateDTO tournamentBetUpdateDTO = TournamentBetUtil.tournamentBetUpdateDTO();
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetService.updateTournamentBet(tournamentBetId, tournamentBetUpdateDTO)).thenReturn(ResponseEntity.ok(tournamentBetDTO));

        ResponseEntity<TournamentBetDTO> response = tournamentBetController.updateTournamentBet(tournamentBetId, tournamentBetUpdateDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(tournamentBetDTO), response);
        verify(tournamentBetService, times(1)).updateTournamentBet(tournamentBetId, tournamentBetUpdateDTO);
    }

    @Test
    public void testDeleteTournamentBet() {
        Long tournamentBetId = 1L;

        when(tournamentBetService.deleteTournamentBet(tournamentBetId)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = tournamentBetController.deleteTournamentBet(tournamentBetId);

        assertNotNull(response);
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(tournamentBetService, times(1)).deleteTournamentBet(tournamentBetId);
    }

    @Test
    public void testGetTournamentBetByUserAndPolla() {
        Long userId = 1L;
        Long pollaId = 1L;
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetService.getTournamentBetByUserAndPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(tournamentBetDTO));

        ResponseEntity<TournamentBetDTO> response = tournamentBetController.getTournamentBetByUserAndPolla(userId, pollaId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(tournamentBetDTO), response);
        verify(tournamentBetService, times(1)).getTournamentBetByUserAndPolla(userId, pollaId);
    }
}