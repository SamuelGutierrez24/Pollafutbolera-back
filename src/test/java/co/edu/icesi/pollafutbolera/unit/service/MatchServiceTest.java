package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.exception.MatchNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.MatchMapper;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.service.MatchServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private StageRepository stageRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Stage stage;
    private Tournament tournament;
    private Team brazil;
    private Team croatia;
    private Match match;
    private MatchDTO matchDTO;

    @BeforeEach
    void setUp() {
        stage = new Stage();
        stage.setId(11L);

        tournament = new Tournament();
        tournament.setId(3L);

        brazil = new Team();
        brazil.setId(37L);
        brazil.setName("Brazil");

        croatia = new Team();
        croatia.setId(24L);
        croatia.setName("Croatia");

        match = new Match();
        match.setId(978072L);
        match.setDate(LocalDateTime.of(2022, 12, 9, 15, 0));
        match.setStatus("Match Finished");
        match.setHomeTeam(croatia);
        match.setAwayTeam(brazil);
        match.setWinnerTeam(croatia);
        match.setTournament(tournament);
        match.setStage(stage);
        match.setDeletedAt(null);

        matchDTO = new MatchDTO(
            978072L,
            LocalDateTime.of(2022, 12, 9, 15, 0),
            "Match Finished",
            24L,
            37L,
            24L,
            3L,
            11L,
            null,
                1,
                1,
                false,
                null,
                null,
                false,
                null,
                null
        );
    }

    @Test
    void testFindAll_Success() {
        when(matchRepository.findAll()).thenReturn(List.of(match));
        when(matchMapper.listToMatchDTO(List.of(match))).thenReturn(List.of(matchDTO));

        ResponseEntity<List<MatchDTO>> response = matchService.findAll();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(978072L, response.getBody().get(0).id());
        verify(matchRepository).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());
        when(matchMapper.listToMatchDTO(Collections.emptyList())).thenReturn(Collections.emptyList());

        ResponseEntity<List<MatchDTO>> response = matchService.findAll();

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
        verify(matchRepository).findAll();
    }

    @Test
    void testFindById_Success() throws Exception {
        when(matchRepository.findById(978072L)).thenReturn(Optional.of(match));
        when(matchMapper.toMatchDTO(match)).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> response = matchService.findById(978072L);

        assertNotNull(response);
        assertEquals(978072L, response.getBody().id());
        verify(matchRepository).findById(978072L);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(matchRepository.findById(978072L)).thenReturn(Optional.empty());

        ResponseEntity<MatchDTO> response = matchService.findById(978072L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(matchRepository).findById(978072L);
    }

    @Test
    void testFindById_Exception() {
        when(matchRepository.findById(978072L)).thenThrow(new RuntimeException("Database Error"));

        Exception exception = assertThrows(Exception.class, () -> matchService.findById(978072L));
        assertEquals("La entidad no existe", exception.getMessage());
        verify(matchRepository).findById(978072L);
    }

    @Test
    void testCreateMatch_Success() {
        // Arrange
        when(matchMapper.toMatch(matchDTO)).thenReturn(match);
        when(matchRepository.save(match)).thenReturn(match);
        when(matchMapper.toMatchDTO(match)).thenReturn(matchDTO);

        // Act
        ResponseEntity<MatchDTO> response = matchService.createMatch(matchDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(matchDTO, response.getBody());

        // Verificar que se llamó a save() una vez
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testCreateMatch_Exception() {
        // Arrange
        when(matchMapper.toMatch(matchDTO)).thenReturn(match);
        when(matchRepository.save(match)).thenThrow(new RuntimeException("Error al guardar"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> matchService.createMatch(matchDTO));
        assertEquals("Error al guardar", exception.getMessage());
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testSave_Success() throws Exception {
        // Arrange
        when(matchRepository.findById(978072L)).thenReturn(Optional.of(match));
        doNothing().when(matchMapper).updateFromDTO(matchDTO, match);
        when(matchRepository.save(match)).thenReturn(match);
        when(matchMapper.toMatchDTO(match)).thenReturn(matchDTO);

        // Act
        ResponseEntity<MatchDTO> response = matchService.save(978072L, matchDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matchDTO, response.getBody());
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testSave_NotFound() {
        // Arrange
        when(matchRepository.findById(978172L)).thenReturn(Optional.empty());

        // Act & Assert
        assertEquals(ResponseEntity.notFound().build(),matchService.save(978172L, matchDTO));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void testFindByStageAndTournament_ReturnsMatch() throws Exception {
        // Mockeamos repos de etapa y torneo
        when(stageRepository.findById(11L)).thenReturn(Optional.of(stage));
        when(tournamentRepository.findById(3L)).thenReturn(Optional.of(tournament));

        // Mockeamos repo de partidos
        when(matchRepository.findByStageAndTournament(stage, tournament))
                .thenReturn(Optional.of(List.of(match)));

        // Mockeamos mapper pa convertir match a dto
        when(matchMapper.listToMatchDTO(List.of(match))).thenReturn(List.of(matchDTO));

        // Metodo a probar
        ResponseEntity<List<MatchDTO>> response = matchService.findByStageAndTournament(11L, 3L);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());

        //Test
        MatchDTO result = response.getBody().get(0);
        assertEquals(978072L, result.id());
        assertEquals("Match Finished", result.status());
        assertEquals(24L, result.homeTeamId());
        assertEquals(37L, result.awayTeamId());
        assertEquals(24L, result.winnerTeamId());
        assertEquals(3L, result.tournamentId());
        assertEquals(11L, result.stageId());

        //System.out.println("ENTRA EN TEST");

        //Todos los metodos se llamaron correctamente
        verify(stageRepository).findById(11L);
        verify(tournamentRepository).findById(3L);
        verify(matchRepository).findByStageAndTournament(stage, tournament);
        verify(matchMapper).listToMatchDTO(List.of(match));
    }

    @Test
    void testFindByTournament_Success() throws Exception {
        when(tournamentRepository.findById(978072L)).thenReturn(Optional.of(tournament));
        when(matchRepository.findByTournament(tournament)).thenReturn(Optional.of(List.of(match)));
        when(matchMapper.listToMatchDTO(List.of(match))).thenReturn(List.of(matchDTO));

        ResponseEntity<List<MatchDTO>> response = matchService.findByTournament(978072L);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(matchRepository, times(1)).findByTournament(tournament);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testFindByTournament_NotFound() throws Exception {
        when(tournamentRepository.findById(978072L)).thenReturn(Optional.of(tournament));
        when(matchRepository.findByTournament(tournament)).thenReturn(Optional.empty());

        ResponseEntity<List<MatchDTO>> response = matchService.findByTournament(978072L);

        assertEquals(404, response.getStatusCodeValue());
        verify(matchRepository, times(1)).findByTournament(tournament);
    }

    @Test
    void testFindByTournament_Exception() {
        when(tournamentRepository.findById(978072L)).thenThrow(new RuntimeException("Error en base de datos"));

        Exception exception = assertThrows(Exception.class, () -> matchService.findByTournament(978072L));

        assertEquals("Match Doesn't found", exception.getMessage());
        verify(tournamentRepository, times(1)).findById(978072L);
    }

    @Test
    void testDeleteById_Success() throws Exception {
        when(matchRepository.findById(978072L)).thenReturn(Optional.of(match));
        doNothing().when(matchRepository).deleteById(978072L);

        assertDoesNotThrow(() -> matchService.deleteById(978072L));
        verify(matchRepository, times(1)).deleteById(978072L);
    }

    @Test
    void testDeleteById_NotFound() {
        when(matchRepository.findById(978072L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> matchService.deleteById(978072L));

        assertEquals("Match Doesn't found", exception.getMessage());
        verify(matchRepository, never()).deleteById(978072L);
    }

    @Test
    void testValidateMatchDate_BrazilVsCroatia() throws Exception {
        // Definir IDs correctos
        Long stageId = 11L;       // Cuartos de final
        Long tournamentId = 3L;   // Copa del Mundo
        Long homeTeamId = 24L;    // Croacia
        Long awayTeamId = 37L;    // Brasil

        // Definir fechas esperadas (9 al 10 de diciembre de 2025)
        LocalDateTime matchDate = LocalDateTime.of(2025, 12, 9, 15, 0);
        LocalDateTime startDate = LocalDateTime.of(2025, 12, 9, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 10, 23, 59);

        // Crear Stage y Tournament simulados
        Stage stage = new Stage();
        stage.setId(stageId);

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);

        // Crear equipos
        Team croatia = new Team();
        croatia.setId(homeTeamId);
        croatia.setName("Croatia");

        Team brazil = new Team();
        brazil.setId(awayTeamId);
        brazil.setName("Brazil");

        // Crear partido simulado
        Match match = new Match();
        match.setId(978072L);
        match.setDate(matchDate);
        match.setStatus("Match Scheduled");
        match.setHomeTeam(croatia);
        match.setAwayTeam(brazil);
        match.setTournament(tournament);
        match.setStage(stage);

        // Mockear repositorios
        when(stageRepository.findById(stageId)).thenReturn(Optional.of(stage));
        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(matchRepository.findByStageAndTournament(stage, tournament))
                .thenReturn(Optional.of(List.of(match)));

        // Mockear el mapeo de la entidad a DTO
        MatchDTO matchDTO = new MatchDTO(978072L, matchDate, "Match Scheduled", homeTeamId, awayTeamId, null, tournamentId, stageId, null,1,
                1,
                false,
                null,
                null,
                false,
                null,
                null);
        when(matchMapper.listToMatchDTO(List.of(match))).thenReturn(List.of(matchDTO));

        // Llamar al método de servicio
        ResponseEntity<List<MatchDTO>> response = matchService.findByStageAndTournament(stageId, tournamentId);
        List<MatchDTO> matches = response.getBody();

        // Validar que el resultado no es nulo y está en el rango de fechas correcto
        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        assertTrue(matches.stream().allMatch(m -> m.date().isAfter(startDate) && m.date().isBefore(endDate)));

        // Verificar que los mocks fueron llamados correctamente
        verify(stageRepository).findById(stageId);
        verify(tournamentRepository).findById(tournamentId);
        verify(matchRepository).findByStageAndTournament(stage, tournament);
        verify(matchMapper).listToMatchDTO(List.of(match));
    }

    
}
