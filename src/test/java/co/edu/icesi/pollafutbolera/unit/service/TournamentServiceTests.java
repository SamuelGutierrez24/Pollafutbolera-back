package co.edu.icesi.pollafutbolera.unit.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.service.TournamentServiceImpl;

public class TournamentServiceTests {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentMapper tournamentMapper;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private Tournament tournament;
    private TournamentDTO tournamentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = Tournament.builder()
                .id(1L)
                .name("Torneo de Colombia")
                .description("Torneo de Colombia")
                .winner_team_id(1L)
                .fewest_goals_conceded_team_id(2L)
                .top_scoring_team_id(3L)
                .build();

        tournamentDTO = new TournamentDTO(
                1L,
                "Torneo de Colombia",
                "Torneo de Colombia",
                LocalDate.now(),
                LocalDate.now(),
                1L,
                2L,
                3L,
                null
        );
    }

    @Test
    void getAllTournaments_ShouldReturnTournamentsList() {
        List<Tournament> tournaments = Arrays.asList(tournament);
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(tournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);

        List<TournamentDTO> result = tournamentService.getAllTournaments().getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tournamentDTO.getId(), result.get(0).getId());
        verify(tournamentRepository).findAll();
        verify(tournamentMapper).toDTO(tournament);
    }

    @Test
    void getTournamentById_WithValidId_ShouldReturnTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);

        TournamentDTO result = tournamentService.getTournamentById(1L).getBody();

        assertNotNull(result);
        assertEquals(tournamentDTO.getId(), result.getId());
        verify(tournamentRepository).findById(1L);
        verify(tournamentMapper).toDTO(tournament);
    }

    @Test
    void getTournamentById_WithInvalidId_ShouldReturnNotFound() {
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        var response = tournamentService.getTournamentById(999L);

        assertEquals(404, response.getStatusCode().value());
        verify(tournamentRepository).findById(999L);
        verifyNoInteractions(tournamentMapper);
    }

    @Test
    void createTournament_WithValidData_ShouldReturnCreatedTournament() {
        when(tournamentMapper.toEntity(tournamentDTO)).thenReturn(tournament);
        when(tournamentRepository.save(tournament)).thenReturn(tournament);
        when(tournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);

        TournamentDTO result = tournamentService.createTournament(tournamentDTO).getBody();

        assertNotNull(result);
        assertEquals(tournamentDTO.getId(), result.getId());
        verify(tournamentMapper).toEntity(tournamentDTO);
        verify(tournamentRepository).save(tournament);
        verify(tournamentMapper).toDTO(tournament);
    }

    @Test
    void updateTournament_WithValidData_ShouldReturnUpdatedTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentMapper.toEntity(tournamentDTO)).thenReturn(tournament);
        when(tournamentRepository.save(tournament)).thenReturn(tournament);
        when(tournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);

        TournamentDTO result = tournamentService.updateTournament(1L, tournamentDTO).getBody();

        assertNotNull(result);
        assertEquals(tournamentDTO.getId(), result.getId());
        verify(tournamentRepository).findById(1L);
        verify(tournamentMapper).toEntity(tournamentDTO);
        verify(tournamentRepository).save(tournament);
        verify(tournamentMapper).toDTO(tournament);
    }

    @Test
    void updateTournament_WithInvalidId_ShouldReturnNotFound() {
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        var response = tournamentService.updateTournament(999L, tournamentDTO);

        assertEquals(404, response.getStatusCode().value());
        verify(tournamentRepository).findById(999L);
        verifyNoInteractions(tournamentMapper);
    }

    @Test
    void deleteTournament_WithValidId_ShouldReturnNoContent() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);

        var response = tournamentService.deleteTournament(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(tournamentRepository).existsById(1L);
        verify(tournamentRepository).deleteById(1L);
    }

    @Test
    void deleteTournament_WithInvalidId_ShouldReturnNotFound() {
        when(tournamentRepository.existsById(999L)).thenReturn(false);

        var response = tournamentService.deleteTournament(999L);

        assertEquals(404, response.getStatusCode().value());
        verify(tournamentRepository).existsById(999L);
        verify(tournamentRepository, never()).deleteById(anyLong());
    }
    
}
