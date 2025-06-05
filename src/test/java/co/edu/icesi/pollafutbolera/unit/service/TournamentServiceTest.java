package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.client.ApiFootballClient;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentStatisticsRepository;
import co.edu.icesi.pollafutbolera.service.TournamentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentStatisticsRepository tournamentStatisticsRepository;

    @Mock
    private TournamentMapper tournamentMapper;


    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApiFootballClient apiFootballClient;


    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private Tournament tournament;
    private TournamentDTO tournamentDTO;
    private Team team;

    @BeforeEach
    void setUp() {
        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("World Cup");
        tournament.setDescription("Copa du mundo");

        tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("World Cup");
        tournamentDTO.setDescription("Copa du mundo");

        team = new Team();
        team.setId(1L);
        team.setName("Test Team");
    }

    @Test
    void updateTournamentStats_ShouldUpdateStatsSuccessfully() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        // Simular los JSONs que devuelve la API (puedes poner JSON de ejemplo simple)
        String standingsJson = """
        {
            "response": [{
                "league": {
                    "logo": "https://example.com/worldcup-logo.png",
                    "standings": [
                        [{
                            "team": {"id": 1118},
                            "points": 30,
                            "all": {
                                "goals": {"for": 50, "against": 10}
                            }
                        }]
                    ]
                }
            }]
        }
    """;

        String topScorersJson = """
        {
            "response": [
                {
                    "player": {
                        "id": 278,
                        "name": "Kylian Mbappé",
                        "photo": "https://example.com/mbappe.jpg"
                    },
                    "statistics": [{
                        "goals": {"total": 20},
                        "team": {"id": 1118}
                    }]
                }
            ]
        }
    """;

        when(apiFootballClient.getStandings(anyLong(), anyLong())).thenReturn(standingsJson);
        when(apiFootballClient.getTopScorers(anyLong(), anyLong())).thenReturn(topScorersJson);

        // Mock el guardado de torneo y estadísticas
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        TournamentStatistics statsSaved = new TournamentStatistics(
                1L, 1118L, 1118L, 1118L, 278L, "Kylian Mbappé", 1118L, "https://example.com/mbappe.jpg");
        when(tournamentStatisticsRepository.save(any(TournamentStatistics.class))).thenReturn(statsSaved);

        TournamentStatsDTO dtoExpected = TournamentStatsDTO.builder()
                .id(1L)
                .tournamentName("World Cup")
                .description("Copa du mundo")
                .tournament_logo("https://example.com/worldcup-logo.png")
                .winnerTeam(1118L)
                .fewestGoalsConcededTeam(1118L)
                .topScoringTeam(1118L)
                .topScorerId(278L)
                .topScorerName("Kylian Mbappé")
                .topScorerTeamId(1118L)
                .topScorerImg("https://example.com/mbappe.jpg")
                .build();

        when(tournamentMapper.toTournamentStatsDTO(any(Tournament.class), anyLong(), any(TournamentStatistics.class)))
                .thenReturn(dtoExpected);

        // Act
        ResponseEntity<TournamentStatsDTO> response = tournamentService.updateTournamentStats(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TournamentStatsDTO actual = response.getBody();
        assertNotNull(actual);
        assertEquals(dtoExpected.id(), actual.id());
        assertEquals(dtoExpected.topScorerName(), actual.topScorerName());
        // ... y demás asserts que quieras

        verify(tournamentRepository, times(1)).findById(1L);
        verify(apiFootballClient, times(1)).getStandings(anyLong(), anyLong());
        verify(apiFootballClient, times(1)).getTopScorers(anyLong(), anyLong());
        verify(tournamentStatisticsRepository, times(1)).save(any(TournamentStatistics.class));
        verify(tournamentMapper, times(1)).toTournamentStatsDTO(any(), anyLong(), any());
    }


    @Test
    void updateTournamentStats_ShouldReturnError_WhenTournamentNotFound() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tournamentService.updateTournamentStats(1L);
        });

        assertTrue(exception.getMessage().contains("Torneo no encontrado"));
        verify(tournamentRepository, times(1)).findById(1L);
        verify(tournamentRepository, never()).save(any(Tournament.class));
        verify(restTemplate, never()).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void tournamentStatsDTO_ShouldBeCreatedWithBuilder() {
        // Test para verificar que el DTO se puede crear correctamente con el builder
        TournamentStatsDTO stats = TournamentStatsDTO.builder()
                .id(1L)
                .tournamentName("Champions League")
                .description("UEFA Champions League")
                .tournament_logo("https://example.com/ucl-logo.png")
                .winnerTeam(100L)
                .fewestGoalsConcededTeam(50L)
                .topScoringTeam(100L)
                .topScorerId(999L)
                .topScorerName("Erling Haaland")
                .topScorerTeamId(100L)
                .topScorerImg("https://example.com/haaland.jpg")
                .build();

        assertNotNull(stats);
        assertEquals(1L, stats.id());
        assertEquals("Champions League", stats.tournamentName());
        assertEquals("UEFA Champions League", stats.description());
        assertEquals("https://example.com/ucl-logo.png", stats.tournament_logo());
        assertEquals(100L, stats.winnerTeam());
        assertEquals(50L, stats.fewestGoalsConcededTeam());
        assertEquals(100L, stats.topScoringTeam());
        assertEquals(999L, stats.topScorerId());
        assertEquals("Erling Haaland", stats.topScorerName());
        assertEquals(100L, stats.topScorerTeamId());
        assertEquals("https://example.com/haaland.jpg", stats.topScorerImg());
    }
}