package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.exception.BetNotAvailableException;
import co.edu.icesi.pollafutbolera.exception.UniqueBetException;
import co.edu.icesi.pollafutbolera.mapper.TournamentBetMapper;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.TournamentBetRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.service.TournamentBetServiceImpl;
import co.edu.icesi.pollafutbolera.util.TournamentBetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TournamentBetServiceTest {

    @Mock
    private TournamentBetRepository tournamentBetRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentBetMapper tournamentBetMapper;

    @InjectMocks
    private TournamentBetServiceImpl tournamentBetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTournamentBetById() {
        Long tournamentBetId = 1L;
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetRepository.findTournamentBetById(tournamentBetId)).thenReturn(tournamentBet);
        when(tournamentBetMapper.toDTO(tournamentBet)).thenReturn(tournamentBetDTO);

        ResponseEntity<TournamentBetDTO> response = tournamentBetService.getTournamentBetById(tournamentBetId);
        TournamentBetDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(tournamentBetId, result.id());
        verify(tournamentBetRepository, times(1)).findTournamentBetById(tournamentBetId);
    }

    @Test
    public void testCreateTournamentBet() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();
        Tournament tournament = Tournament.builder()
                .id(1L)
                .initial_date(LocalDate.now().plusDays(1))
                .build();

        when(tournamentBetMapper.toEntity(tournamentBetDTO)).thenReturn(tournamentBet);
        when(tournamentBetRepository.existsByUserAndPolla(tournamentBet.getUser().getId(), tournamentBet.getPolla().getId())).thenReturn(false);
        when(tournamentRepository.findById(tournamentBet.getTournament().getId())).thenReturn(Optional.of(tournament));
        when(tournamentBetRepository.save(tournamentBet)).thenReturn(tournamentBet);
        when(tournamentBetMapper.toDTO(tournamentBet)).thenReturn(tournamentBetDTO);

        ResponseEntity<TournamentBetDTO> response = tournamentBetService.createTournamentBet(tournamentBetDTO);
        TournamentBetDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(tournamentBetDTO.id(), result.id());
        verify(tournamentRepository, times(1)).findById(tournamentBet.getTournament().getId());
        verify(tournamentBetRepository, times(1)).save(tournamentBet);
    }

    @Test
    public void testUpdateTournamentBet() {
        Long tournamentBetId = 1L;
        TournamentBetUpdateDTO updateDTO = TournamentBetUtil.tournamentBetUpdateDTO();
        TournamentBet existingTournamentBet = TournamentBetUtil.tournamentBet();
        TournamentBet updatedTournamentBet = TournamentBetUtil.tournamentBet();
        TournamentBetDTO updatedTournamentBetDTO = TournamentBetUtil.tournamentBetDTO();
        Tournament tournament = Tournament.builder()
                .id(1L)
                .initial_date(LocalDate.now().plusDays(1))
                .build();

        when(tournamentBetRepository.findById(tournamentBetId)).thenReturn(Optional.of(existingTournamentBet));
        when(tournamentRepository.findById(existingTournamentBet.getTournament().getId())).thenReturn(Optional.of(tournament));
        when(tournamentBetMapper.toDTO(existingTournamentBet)).thenReturn(updatedTournamentBetDTO);
        when(tournamentBetMapper.toEntity(updatedTournamentBetDTO)).thenReturn(updatedTournamentBet);
        when(tournamentBetRepository.save(updatedTournamentBet)).thenReturn(updatedTournamentBet);
        when(tournamentBetMapper.toDTO(updatedTournamentBet)).thenReturn(updatedTournamentBetDTO);

        ResponseEntity<TournamentBetDTO> response = tournamentBetService.updateTournamentBet(tournamentBetId, updateDTO);
        TournamentBetDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(updatedTournamentBetDTO.id(), result.id());
        verify(tournamentRepository, times(1)).findById(existingTournamentBet.getTournament().getId());
        verify(tournamentBetRepository, times(1)).findById(tournamentBetId);
        verify(tournamentBetRepository, times(1)).save(updatedTournamentBet);
    }

    @Test
    public void testDeleteTournamentBet() {
        Long tournamentBetId = 1L;

        when(tournamentBetRepository.existsById(tournamentBetId)).thenReturn(true);
        doNothing().when(tournamentBetRepository).deleteById(tournamentBetId);

        ResponseEntity<Void> response = tournamentBetService.deleteTournamentBet(tournamentBetId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tournamentBetRepository, times(1)).existsById(tournamentBetId);
        verify(tournamentBetRepository, times(1)).deleteById(tournamentBetId);
    }

    @Test
    public void testDeleteTournamentBet_NotFound() {
        Long tournamentBetId = 1L;

        when(tournamentBetRepository.existsById(tournamentBetId)).thenReturn(false);

        ResponseEntity<Void> response = tournamentBetService.deleteTournamentBet(tournamentBetId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tournamentBetRepository, times(1)).existsById(tournamentBetId);
        verify(tournamentBetRepository, times(0)).deleteById(tournamentBetId);
    }

    @Test
    public void testCreateTournamentBet_UniqueBetException() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();

        when(tournamentBetMapper.toEntity(tournamentBetDTO)).thenReturn(tournamentBet);
        when(tournamentBetRepository.existsByUserAndPolla(tournamentBet.getUser().getId(), tournamentBet.getPolla().getId())).thenReturn(true);

        UniqueBetException exception = assertThrows(UniqueBetException.class, () -> {
            tournamentBetService.createTournamentBet(tournamentBetDTO);
        });

        assertNotNull(exception);
        verify(tournamentBetRepository, times(1)).existsByUserAndPolla(tournamentBet.getUser().getId(), tournamentBet.getPolla().getId());
        verify(tournamentRepository, times(0)).findById(any());
        verify(tournamentBetRepository, times(0)).save(tournamentBet);
    }

    @Test
    public void testCreateTournamentBet_DataIntegrityViolationException() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();
        Tournament tournament = Tournament.builder()
                .id(1L)
                .initial_date(LocalDate.now().plusDays(1))
                .build();

        when(tournamentBetMapper.toEntity(tournamentBetDTO)).thenReturn(tournamentBet);
        when(tournamentBetRepository.existsByUserAndPolla(tournamentBet.getUser().getId(), tournamentBet.getPolla().getId())).thenReturn(false);
        when(tournamentRepository.findById(tournamentBet.getTournament().getId())).thenReturn(Optional.of(tournament));
        when(tournamentBetRepository.save(tournamentBet)).thenThrow(new DataIntegrityViolationException(""));

        ResponseEntity<TournamentBetDTO> response = tournamentBetService.createTournamentBet(tournamentBetDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tournamentRepository, times(1)).findById(tournamentBet.getTournament().getId());
        verify(tournamentBetRepository, times(1)).save(tournamentBet);
    }

    @Test
    public void testGetTournamentBetByUserAndPolla() {
        Long userId = 1L;
        Long pollaId = 1L;
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        when(tournamentBetRepository.findTournamentBetByUserAndPolla(userId, pollaId)).thenReturn(tournamentBet);
        when(tournamentBetMapper.toDTO(tournamentBet)).thenReturn(tournamentBetDTO);

        ResponseEntity<TournamentBetDTO> response = tournamentBetService.getTournamentBetByUserAndPolla(userId, pollaId);
        TournamentBetDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(tournamentBetDTO.id(), result.id());
        verify(tournamentBetRepository, times(1)).findTournamentBetByUserAndPolla(userId, pollaId);
    }

    @Test
    public void testUpdateEarnedPointsByUserAndPolla() {
        Long userId = 1L;
        Long pollaId = 1L;
        int earnedPoints = 15;

        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();

        when(tournamentBetRepository.findTournamentBetByUserAndPolla(userId, pollaId))
                .thenReturn(tournamentBet);

        tournamentBetService.updateEarnedPointsByUserAndPolla(userId, pollaId, earnedPoints);

        assertEquals(earnedPoints, tournamentBet.getEarnedPoints());
        verify(tournamentBetRepository, times(1)).findTournamentBetByUserAndPolla(userId, pollaId);
        verify(tournamentBetRepository, times(1)).save(tournamentBet);
    }

    @Test
    public void testCreateTournamentBet_TournamentDateValidation_Fails() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();
        Tournament tournament = Tournament.builder()
                .id(1L)
                .initial_date(LocalDate.now().minusDays(1))
                .build();

        when(tournamentBetMapper.toEntity(tournamentBetDTO)).thenReturn(tournamentBet);
        when(tournamentBetRepository.existsByUserAndPolla(tournamentBet.getUser().getId(), tournamentBet.getPolla().getId())).thenReturn(false);
        when(tournamentRepository.findById(tournamentBet.getTournament().getId())).thenReturn(Optional.of(tournament));

        BetNotAvailableException exception = assertThrows(BetNotAvailableException.class, () -> {
            tournamentBetService.createTournamentBet(tournamentBetDTO);
        });

        assertNotNull(exception);
        verify(tournamentRepository, times(1)).findById(tournamentBet.getTournament().getId());
        verify(tournamentBetRepository, times(0)).save(tournamentBet);
    }

    @Test
    public void testUpdateTournamentBet_TournamentDateValidation_Fails() {
        Long tournamentBetId = 1L;
        TournamentBetUpdateDTO updateDTO = TournamentBetUtil.tournamentBetUpdateDTO();
        TournamentBet existingTournamentBet = TournamentBetUtil.tournamentBet();
        Tournament tournament = Tournament.builder()
                .id(1L)
                .initial_date(LocalDate.now().minusDays(1))
                .build();

        when(tournamentBetRepository.findById(tournamentBetId)).thenReturn(Optional.of(existingTournamentBet));
        when(tournamentRepository.findById(existingTournamentBet.getTournament().getId())).thenReturn(Optional.of(tournament));

        BetNotAvailableException exception = assertThrows(BetNotAvailableException.class, () -> {
            tournamentBetService.updateTournamentBet(tournamentBetId, updateDTO);
        });

        assertNotNull(exception);
        verify(tournamentRepository, times(1)).findById(existingTournamentBet.getTournament().getId());
        verify(tournamentBetRepository, times(0)).save(any());
    }
}
