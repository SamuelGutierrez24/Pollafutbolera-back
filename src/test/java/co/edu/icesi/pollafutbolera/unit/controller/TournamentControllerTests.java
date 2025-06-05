package co.edu.icesi.pollafutbolera.unit.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.edu.icesi.pollafutbolera.controller.TournamentController;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.service.TournamentService;
import co.edu.icesi.pollafutbolera.util.TournamentUtil;

public class TournamentControllerTests {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private Tournament tournament;
    private TournamentDTO tournamentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = TournamentUtil.tournament();
        tournamentDTO = new TournamentDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDescription(),
                tournament.getInitial_date(),
                tournament.getFinal_date(),
            tournament.getWinner_team_id(),
            tournament.getFewest_goals_conceded_team_id(),
            tournament.getTop_scoring_team_id(),
            null
        );
    }

    @Test
    void createTournament_WithValidData_ShouldReturnCreatedTournament() {
        when(tournamentService.createTournament(tournamentDTO)).thenReturn(ResponseEntity.ok(tournamentDTO));

        ResponseEntity<TournamentDTO> response = tournamentController.createTournament(tournamentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO.getId(), response.getBody().getId());
        verify(tournamentService).createTournament(tournamentDTO);
    }

    @Test
    void createTournament_WithNullData_ShouldReturnBadRequest() {
        when(tournamentService.createTournament(null)).thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<TournamentDTO> response = tournamentController.createTournament(null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tournamentService).createTournament(null);
    }

    @Test
    void createTournament_WithNullId_ShouldReturnBadRequest() {
        TournamentDTO invalidTournamentDTO = new TournamentDTO(null, "TorneoPrueba", null, null, null, null, null, null,null);
        when(tournamentService.createTournament(invalidTournamentDTO)).thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<TournamentDTO> response = tournamentController.createTournament(invalidTournamentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tournamentService).createTournament(invalidTournamentDTO);
    }

    @Test
    void createTournament_WithInvalidDTO_ShouldReturnBadRequest() {
        TournamentDTO invalidDTO = new TournamentDTO(null, "", "", null, null, null, null,null,null);

        when(tournamentService.createTournament(invalidDTO)).thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<TournamentDTO> response = tournamentController.createTournament(invalidDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tournamentService).createTournament(invalidDTO);
    }

    @Test
    void getAllTournaments_ShouldReturnTournamentsList() {
        List<TournamentDTO> tournaments = Arrays.asList(tournamentDTO);
        when(tournamentService.getAllTournaments()).thenReturn(ResponseEntity.ok(tournaments));

        ResponseEntity<List<TournamentDTO>> response = tournamentController.getAllTournaments();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(tournamentService).getAllTournaments();
    }

    @Test
    void getAllTournaments_NoTournaments_ShouldReturnEmptyList() {
        List<TournamentDTO> tournaments = Arrays.asList();
        when(tournamentService.getAllTournaments()).thenReturn(ResponseEntity.ok(tournaments));

        ResponseEntity<List<TournamentDTO>> response = tournamentController.getAllTournaments();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(tournamentService).getAllTournaments();
    }

    @Test
    void getTournamentById_WithValidId_ShouldReturnTournament() {
        when(tournamentService.getTournamentById(1L)).thenReturn(ResponseEntity.ok(tournamentDTO));

        ResponseEntity<TournamentDTO> response = tournamentController.getTournamentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO.getId(), response.getBody().getId());
        verify(tournamentService).getTournamentById(1L);
    }

    @Test
    void getTournamentById_WithInvalidId_ShouldReturnNotFound(){
        when(tournamentService.getTournamentById(999L)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<TournamentDTO> response = tournamentController.getTournamentById(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tournamentService).getTournamentById(999L);
    }

    @Test
    void getTournamentById_WithValidId_ShouldReturnCorrectTournament() {
        when(tournamentService.getTournamentById(1L)).thenReturn(ResponseEntity.ok(tournamentDTO));

        ResponseEntity<TournamentDTO> response = tournamentController.getTournamentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO.getId(), response.getBody().getId());
        assertEquals(tournamentDTO.getName(), response.getBody().getName());
        verify(tournamentService).getTournamentById(1L);
    }

    @Test
    void updateTournament_WithValidData_ShouldReturnUpdatedTournament() {
        when(tournamentService.updateTournament(1L, tournamentDTO))
            .thenReturn(ResponseEntity.ok(tournamentDTO));

        ResponseEntity<TournamentDTO> response = tournamentController.updateTournament(1L, tournamentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournamentDTO.getId(), response.getBody().getId());
        verify(tournamentService).updateTournament(1L, tournamentDTO);
    }

    @Test
    void updateTournament_WithNoTournament_ShouldReturnNotFound() {
        when(tournamentService.updateTournament(999L, tournamentDTO)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<TournamentDTO> response = tournamentController.updateTournament(999L, tournamentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tournamentService).updateTournament(999L, tournamentDTO);
    }

    @Test
    void updateTournament_WithNullData_ShouldReturnBadRequest() {
        when(tournamentService.updateTournament(1L, null)).thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<TournamentDTO> response = tournamentController.updateTournament(1L, null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tournamentService).updateTournament(1L, null);
    }

    @Test
    void deleteTournament_WithValidId_ShouldReturnNoContent() {
        when(tournamentService.deleteTournament(1L)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = tournamentController.deleteTournament(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tournamentService).deleteTournament(1L);
    }

    @Test
    void deleteTournament_WithNoTournament_ShouldReturnNotFound(){
        when(tournamentService.deleteTournament(999L)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<Void> response = tournamentController.deleteTournament(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tournamentService).deleteTournament(999L);
    }
}
