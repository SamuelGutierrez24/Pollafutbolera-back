package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.exception.BetNotAvailableException;
import co.edu.icesi.pollafutbolera.exception.UniqueBetException;
import co.edu.icesi.pollafutbolera.mapper.MatchBetMapper;
import co.edu.icesi.pollafutbolera.model.MatchBet;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.MatchBetRepository;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.service.MatchBetServiceImpl;
import co.edu.icesi.pollafutbolera.util.MatchBetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchBetServiceTest {

    @Mock
    private MatchBetRepository matchBetRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchBetMapper matchBetMapper;

    @InjectMocks
    private MatchBetServiceImpl matchBetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMatchBetById() {
        Long matchBetId = 1L;
        MatchBet matchBet = MatchBetUtil.matchBet();
        MatchBetResponseDTO matchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();

        when(matchBetRepository.findMatchBetById(matchBetId)).thenReturn(matchBet);
        when(matchBetMapper.toResponseDTO(matchBet)).thenReturn(matchBetResponseDTO);

        ResponseEntity<MatchBetResponseDTO> response = matchBetService.getMatchBetById(matchBetId);
        MatchBetResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(matchBetId, result.id());
        verify(matchBetRepository, times(1)).findMatchBetById(matchBetId);
    }

    @Test
    public void testCreateMatchBet() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();
        MatchBet matchBet = MatchBetUtil.matchBet();
        MatchBetResponseDTO matchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().plusHours(10))
                .build();

        when(matchBetMapper.toEntity(matchBetDTO)).thenReturn(matchBet);
        when(matchBetRepository.existsByUserAndMatchAndPolla(matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId())).thenReturn(false);
        when(matchRepository.findById(matchBet.getMatch().getId())).thenReturn(Optional.of(match));
        when(matchBetRepository.save(matchBet)).thenReturn(matchBet);
        when(matchBetMapper.toResponseDTO(matchBet)).thenReturn(matchBetResponseDTO);

        ResponseEntity<MatchBetResponseDTO> response = matchBetService.createMatchBet(matchBetDTO);
        MatchBetResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(matchBetResponseDTO.id(), result.id());
        verify(matchBetRepository, times(1)).save(matchBet);
    }

    @Test
    public void testCreateMatchBet_TournamentDateValidation() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();
        MatchBet matchBet = MatchBetUtil.matchBet();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().plusHours(10))
                .tournament(Tournament.builder()
                        .id(1L)
                        .initial_date(LocalDateTime.now().minusDays(1).toLocalDate())
                        .build())
                .build();

        when(matchBetMapper.toEntity(matchBetDTO)).thenReturn(matchBet);
        when(matchBetRepository.existsByUserAndMatchAndPolla(matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId())).thenReturn(false);
        when(matchRepository.findById(matchBet.getMatch().getId())).thenReturn(Optional.of(match));
        when(matchBetRepository.save(matchBet)).thenReturn(matchBet);
        when(matchBetMapper.toResponseDTO(matchBet)).thenReturn(MatchBetUtil.matchBetResponseDTO());

        ResponseEntity<MatchBetResponseDTO> response = matchBetService.createMatchBet(matchBetDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchRepository, times(1)).findById(matchBet.getMatch().getId());
    }

    @Test
    public void testCreateMatchBet_TournamentDateValidation_Fails() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();
        MatchBet matchBet = MatchBetUtil.matchBet();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().minusHours(10))
                .tournament(Tournament.builder()
                        .id(1L)
                        .initial_date(LocalDateTime.now().plusDays(1).toLocalDate())
                        .build())
                .build();

        when(matchBetMapper.toEntity(matchBetDTO)).thenReturn(matchBet);
        when(matchBetRepository.existsByUserAndMatchAndPolla(matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId())).thenReturn(false);
        when(matchRepository.findById(matchBet.getMatch().getId())).thenReturn(Optional.of(match));

        BetNotAvailableException exception = assertThrows(BetNotAvailableException.class, () -> {
            matchBetService.createMatchBet(matchBetDTO);
        });

        assertNotNull(exception);
        verify(matchRepository, times(1)).findById(matchBet.getMatch().getId());
    }

    @Test
    public void testUpdateMatchBet() {
        Long matchBetId = 1L;
        MatchBetUpdateDTO updateDTO = MatchBetUpdateDTO.builder()
                .homeScore(3)
                .awayScore(2)
                .build();
        MatchBet existingMatchBet = MatchBetUtil.matchBet();
        MatchBet updatedMatchBet = MatchBetUtil.matchBet();
        updatedMatchBet.setHomeScore(3);
        updatedMatchBet.setAwayScore(2);
        MatchBetResponseDTO updatedMatchBetResponseDTO = MatchBetUtil.matchBetResponseDTO();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().plusHours(10))
                .build();

        when(matchBetRepository.findById(matchBetId)).thenReturn(Optional.of(existingMatchBet));
        when(matchRepository.findById(existingMatchBet.getMatch().getId())).thenReturn(Optional.of(match));
        doNothing().when(matchBetMapper).updateMatchBetFromDTO(updateDTO, existingMatchBet);
        when(matchBetRepository.save(existingMatchBet)).thenReturn(updatedMatchBet);
        when(matchBetMapper.toResponseDTO(updatedMatchBet)).thenReturn(updatedMatchBetResponseDTO);

        ResponseEntity<MatchBetResponseDTO> response = matchBetService.updateMatchBet(matchBetId, updateDTO);
        MatchBetResponseDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(updatedMatchBetResponseDTO.homeScore(), result.homeScore());
        assertEquals(updatedMatchBetResponseDTO.awayScore(), result.awayScore());
        verify(matchBetRepository, times(1)).findById(matchBetId);
        verify(matchBetRepository, times(1)).save(existingMatchBet);
    }

    @Test
    public void testUpdateMatchBet_TournamentDateValidation() {
        Long matchBetId = 1L;
        MatchBetUpdateDTO updateDTO = MatchBetUtil.matchBetUpdateDTO();
        MatchBet existingMatchBet = MatchBetUtil.matchBet();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().plusHours(10))
                .tournament(Tournament.builder()
                        .id(1L)
                        .initial_date(LocalDateTime.now().minusDays(1).toLocalDate())
                        .build())
                .build();
        existingMatchBet.setMatch(match);

        when(matchBetRepository.findById(matchBetId)).thenReturn(Optional.of(existingMatchBet));
        when(matchRepository.findById(existingMatchBet.getMatch().getId())).thenReturn(Optional.of(match));
        doNothing().when(matchBetMapper).updateMatchBetFromDTO(updateDTO, existingMatchBet);
        when(matchBetRepository.save(existingMatchBet)).thenReturn(existingMatchBet);
        when(matchBetMapper.toResponseDTO(existingMatchBet)).thenReturn(MatchBetUtil.matchBetResponseDTO());

        ResponseEntity<MatchBetResponseDTO> response = matchBetService.updateMatchBet(matchBetId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchRepository, times(1)).findById(existingMatchBet.getMatch().getId());
    }

    @Test
    public void testUpdateMatchBet_TournamentDateValidation_Fails() {
        Long matchBetId = 1L;
        MatchBetUpdateDTO updateDTO = MatchBetUtil.matchBetUpdateDTO();
        MatchBet existingMatchBet = MatchBetUtil.matchBet();
        Match match = Match.builder()
                .id(1L)
                .date(LocalDateTime.now().minusHours(10))
                .tournament(Tournament.builder()
                        .id(1L)
                        .initial_date(LocalDateTime.now().plusDays(1).toLocalDate())
                        .build())
                .build();
        existingMatchBet.setMatch(match);

        when(matchBetRepository.findById(matchBetId)).thenReturn(Optional.of(existingMatchBet));
        when(matchRepository.findById(existingMatchBet.getMatch().getId())).thenReturn(Optional.of(match));

        BetNotAvailableException exception = assertThrows(BetNotAvailableException.class, () -> {
            matchBetService.updateMatchBet(matchBetId, updateDTO);
        });

        assertNotNull(exception);
        verify(matchRepository, times(1)).findById(existingMatchBet.getMatch().getId());
    }

    @Test
    public void testDeleteMatchBet() {
        Long matchBetId = 1L;

        when(matchBetRepository.existsById(matchBetId)).thenReturn(true);
        doNothing().when(matchBetRepository).deleteById(matchBetId);

        ResponseEntity<Void> response = matchBetService.deleteMatchBet(matchBetId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(matchBetRepository, times(1)).existsById(matchBetId);
        verify(matchBetRepository, times(1)).deleteById(matchBetId);
    }

    @Test
    public void testDeleteMatchBet_NotFound() {
        Long matchBetId = 1L;

        when(matchBetRepository.existsById(matchBetId)).thenReturn(false);

        ResponseEntity<Void> response = matchBetService.deleteMatchBet(matchBetId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(matchBetRepository, times(1)).existsById(matchBetId);
        verify(matchBetRepository, times(0)).deleteById(matchBetId);
    }

    @Test
    public void testGetMatchBetsByUserAndPolla() {
        Long userId = 1L;
        Long pollaId = 1L;
        MatchBet matchBet1 = MatchBetUtil.matchBet();
        MatchBet matchBet2 = MatchBetUtil.matchBet();
        matchBet2.setId(2L);
        List<MatchBet> matchBets = Arrays.asList(matchBet1, matchBet2);
        MatchBetResponseDTO matchBetResponseDTO1 = MatchBetUtil.matchBetResponseDTO();
        MatchBetResponseDTO matchBetResponseDTO2 = MatchBetUtil.matchBetResponseDTO();


        when(matchBetRepository.findMatchBetsByUserAndPolla(userId, pollaId)).thenReturn(matchBets);
        when(matchBetMapper.toResponseDTO(matchBet1)).thenReturn(matchBetResponseDTO1);
        when(matchBetMapper.toResponseDTO(matchBet2)).thenReturn(matchBetResponseDTO2);

        ResponseEntity<List<MatchBetResponseDTO>> response = matchBetService.getMatchBetsByUserAndPolla(userId, pollaId);
        List<MatchBetResponseDTO> result = response.getBody();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(matchBetRepository, times(1)).findMatchBetsByUserAndPolla(userId, pollaId);
    }

    @Test
    public void testGetMatchBetsByUser() {
        Long userId = 1L;
        MatchBet matchBet1 = MatchBetUtil.matchBet();
        MatchBet matchBet2 = MatchBetUtil.matchBet();
        matchBet2.setId(2L);
        List<MatchBet> matchBets = Arrays.asList(matchBet1, matchBet2);
        MatchBetResponseDTO matchBetResponseDTO1 = MatchBetUtil.matchBetResponseDTO();
        MatchBetResponseDTO matchBetResponseDTO2 = MatchBetUtil.matchBetResponseDTO();

        when(matchBetRepository.findMatchBetsByUser(userId)).thenReturn(matchBets);
        when(matchBetMapper.toResponseDTO(matchBet1)).thenReturn(matchBetResponseDTO1);
        when(matchBetMapper.toResponseDTO(matchBet2)).thenReturn(matchBetResponseDTO2);

        ResponseEntity<List<MatchBetResponseDTO>> response = matchBetService.getMatchBetsByUser(userId);
        List<MatchBetResponseDTO> result = response.getBody();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(matchBetRepository, times(1)).findMatchBetsByUser(userId);
    }

    @Test
    public void testCreateMatchBet_UniqueBetException() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();
        MatchBet matchBet = MatchBetUtil.matchBet();

        when(matchBetMapper.toEntity(matchBetDTO)).thenReturn(matchBet);
        when(matchBetRepository.existsByUserAndMatchAndPolla(matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId())).thenReturn(true);

        UniqueBetException exception = assertThrows(UniqueBetException.class, () -> {
            matchBetService.createMatchBet(matchBetDTO);
        });

        assertNotNull(exception);
        verify(matchBetRepository, times(1)).existsByUserAndMatchAndPolla(matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId());
        verify(matchBetRepository, times(0)).save(matchBet);
    }

}

