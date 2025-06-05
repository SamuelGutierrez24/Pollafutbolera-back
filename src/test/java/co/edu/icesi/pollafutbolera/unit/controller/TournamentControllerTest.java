package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.TournamentController;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import co.edu.icesi.pollafutbolera.service.TournamentRegistrationService;
import co.edu.icesi.pollafutbolera.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @Mock
    private TournamentRegistrationService tournamentRegistrationService;

    @InjectMocks
    private TournamentController tournamentController;

    private TournamentDTO tournamentDTO;
    private TournamentStatsDTO tournamentStatsDTO;
    private Tournament tournament;
    private TournamentStatistics tournamentStatistics;

    @BeforeEach
    void setUp() {
        tournamentDTO = TournamentDTO.builder()
                .id(1L)
                .name("Premier League")
                .description("English top division league")
                .initial_date(LocalDate.now())
                .final_date(LocalDate.now().plusDays(30))
                .build();

        tournamentStatsDTO = new TournamentStatsDTO(
                1L, "World Cup", "Torneo de World",
                "https://media.api-sports.io/football/leagues/1.png",
                1118L, 1118L, 10L, 278L,
                "Kylian Mbappé", 2L,
                "https://media.api-sports.io/football/players/278.png"
        );

        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Premier League");

        tournamentStatistics = new TournamentStatistics();
        tournamentStatistics.setTournamentId(1L);
    }

    @Test
    void getAllTournaments_ShouldReturnListOfTournaments() {
        when(tournamentService.getAllTournaments()).thenReturn(ResponseEntity.ok(List.of(tournamentDTO)));

        var response = tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(tournamentService).getAllTournaments();
    }

    @Test
    void getTournamentById_ShouldReturnTournament() {
        when(tournamentService.getTournamentById(1L)).thenReturn(ResponseEntity.ok(tournamentDTO));

        var response = tournamentController.getTournamentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Premier League", response.getBody().getName());
        verify(tournamentService).getTournamentById(1L);
    }

    @Test
    void createTournament_ShouldReturnCreatedTournament() {
        when(tournamentService.createTournament(tournamentDTO)).thenReturn(ResponseEntity.ok(tournamentDTO));

        var response = tournamentController.createTournament(tournamentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Premier League", response.getBody().getName());
        verify(tournamentService).createTournament(tournamentDTO);
    }

    @Test
    void updateTournament_ShouldReturnUpdatedTournament() {
        when(tournamentService.updateTournament(1L, tournamentDTO)).thenReturn(ResponseEntity.ok(tournamentDTO));

        var response = tournamentController.updateTournament(1L, tournamentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Premier League", response.getBody().getName());
        verify(tournamentService).updateTournament(1L, tournamentDTO);
    }

    @Test
    void deleteTournament_ShouldReturnOk() {
        when(tournamentService.deleteTournament(1L)).thenReturn(ResponseEntity.ok().build());

        var response = tournamentController.deleteTournament(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tournamentService).deleteTournament(1L);
    }

    @Test
    void getStatistics_ShouldReturnStats() {
        when(tournamentService.getTournamentStatistics(1L)).thenReturn(ResponseEntity.ok(tournamentStatistics));

        var response = tournamentController.getStatistics(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getTournamentId());
        verify(tournamentService).getTournamentStatistics(1L);
    }

    @Test
    void getTournament_ShouldReturnTournament() {
        when(tournamentService.getTournament(1L)).thenReturn(ResponseEntity.ok(tournament));

        var response = tournamentController.getTournament(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(tournamentService).getTournament(1L);
    }

    @Test
    void updateTournamentStats_ShouldReturnStats() {
        when(tournamentService.updateTournamentStats(1L)).thenReturn(ResponseEntity.ok(tournamentStatsDTO));

        var response = tournamentController.updateTournamentStats(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("World Cup", response.getBody().tournamentName());
        verify(tournamentService).updateTournamentStats(1L);
    }

    @Test
    void register_ShouldReturnRegisteredTournament() throws Exception {
        when(tournamentRegistrationService.registerTournament(39L, 2024))
                .thenReturn(ResponseEntity.ok(tournamentDTO));

        var response = tournamentController.register(39L, 2024);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Premier League", response.getBody().getName());
        verify(tournamentRegistrationService).registerTournament(39L, 2024);
    }
}
