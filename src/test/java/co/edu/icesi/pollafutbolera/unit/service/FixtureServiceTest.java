package co.edu.icesi.pollafutbolera.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import co.edu.icesi.pollafutbolera.client.ApiFootballClient;
import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import co.edu.icesi.pollafutbolera.dto.StatisticDTO;
import co.edu.icesi.pollafutbolera.dto.TeamStaticsDTO;
import co.edu.icesi.pollafutbolera.mapper.MatchStatsMapper;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.MatchStats;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.MatchStatsRepository;
import co.edu.icesi.pollafutbolera.service.FixtureServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FixtureServiceTest {

    @Mock private MatchStatsRepository matchStatsRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private MatchStatsMapper matchStatsMapper;
    @Mock private ApiFootballClient apiFootballClient;

    @InjectMocks private FixtureServiceImpl fixtureService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception ex = assertThrows(Exception.class, () -> fixtureService.fixtureStatics(1L));
        assertEquals("Match not found", ex.getMessage());
    }

    @Test
    void shouldReturnFixturesFromDatabaseIfPresent() throws Exception {
        // Given
        Match match = new Match();
        MatchStats stat = new MatchStats();
        FixtureDTO dto = new FixtureDTO();

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchStatsRepository.findByMatchId(match)).thenReturn(List.of(stat));
        when(matchStatsMapper.completeFixtureDTO(stat)).thenReturn(dto);

        // When
        List<FixtureDTO> result = fixtureService.fixtureStatics(1L);

        // Then
        assertEquals(1, result.size());
        verify(apiFootballClient, never()).getFixtureStatistics(anyLong());
    }

    @Test
    void shouldFetchFromApiIfStatsAreEmpty() throws Exception {
        // Given
        Match match = new Match();
        match.setId(1L);

        FixtureDTO fixtureDTO = new FixtureDTO();
        fixtureDTO.setTeam(new TeamStaticsDTO(10L, "Real Madrid", "logo.png"));
        fixtureDTO.setStatistics(List.of(new StatisticDTO("Shots on Goal", "5")));

        List<FixtureDTO> dtoList = List.of(fixtureDTO);
        String jsonBody = objectMapper.writeValueAsString(Map.of("response", dtoList));
        ResponseEntity<String> response = new ResponseEntity<>(jsonBody, HttpStatus.OK);

        MatchStats mockStats = new MatchStats();
        mockStats.setTeamName("Real Madrid");
        mockStats.setShotsOnGoal(5); // o cualquier dato relevante

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchStatsRepository.findByMatchId(match)).thenReturn(Collections.emptyList());
        when(apiFootballClient.getFixtureStatistics(1L)).thenReturn(response);
        when(matchStatsMapper.completeMatchStats(any(FixtureDTO.class))).thenReturn(mockStats);
        when(matchStatsRepository.save(any())).thenReturn(mockStats);

        // When
        List<FixtureDTO> result = fixtureService.fixtureStatics(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("Real Madrid", result.get(0).getTeam().name());
        verify(matchStatsRepository).save(any(MatchStats.class));
    }
}

