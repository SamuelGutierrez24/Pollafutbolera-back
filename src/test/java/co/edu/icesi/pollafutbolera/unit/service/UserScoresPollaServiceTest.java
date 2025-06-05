package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.enums.UserPollaState;
import co.edu.icesi.pollafutbolera.exception.UserNotInPollaException;
import co.edu.icesi.pollafutbolera.mapper.UserMapper;
import co.edu.icesi.pollafutbolera.mapper.UserScoresPollaMapper;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.*;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import co.edu.icesi.pollafutbolera.service.*;
import co.edu.icesi.pollafutbolera.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class UserScoresPollaServiceTest {

    @Mock
    private UserScoresPollaRepository userScoresPollaRepository;

    @InjectMocks
    private UserScoresPollaServiceImpl userScoresPollaService;

    @InjectMocks
    private PollaServiceImpl pollaService;

    @Mock
    private PollaRepository pollaRepository;

    @Mock
    private SubPollaRepository subPollaRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserScoresPollaMapper userScoresPollaMapper;

    @Mock
    private TournamentBetRepository tournamentBetRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchBetRepository matchBetRepository;

    @Mock
    private TournamentBetServiceImpl tournamentBetService;

    @Mock
    private TournamentStatisticsRepository tournamentStatisticsRepository;

    @InjectMocks
    private ScoresEnd scoresEnd;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User user = UserUtil.user();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userService.getUserById(userId);
        UserDTO result = response.getBody();

        assertNotNull(result);
        assertEquals(userId, result.ID());
        verify(userRepository, times(1)).findById(userId);
    }
     */

    /*
    @Test
    public void testGetUsersByPollaId() throws Exception {
        Long pollaId = 1L;

        List<UserScoresPolla> userScoresPollas = List.of(
                UserScoresPollaUtil.userScoresPolla(),
                UserScoresPollaUtil.userScoresPolla2()
        );

        List<UserDTO> expectedUsers = userScoresPollas.stream()
                .map(userScoresPolla -> new UserDTO(
                        userScoresPolla.getUser().getId(),
                        userScoresPolla.getUser().getCedula(),
                        userScoresPolla.getUser().getCompany().getId(),
                        userScoresPolla.getUser().getName(),
                        userScoresPolla.getUser().getLastName(),
                        userScoresPolla.getUser().getPassword(),
                        userScoresPolla.getUser().getMail(),
                        userScoresPolla.getUser().getNickname(),
                        userScoresPolla.getUser().getPhoneNumber(),
                        userScoresPolla.getUser().getPhoto(),
                        userScoresPolla.getUser().getRole().getId(),
                        userScoresPolla.getUser().getExtraInfo()
                ))
                .toList();

        when(userScoresPollaRepository.findByPollaId(pollaId)).thenReturn(userScoresPollas);

        for (int i = 0; i < userScoresPollas.size(); i++) {
            when(userMapper.toDTO(userScoresPollas.get(i).getUser()))
                    .thenReturn(expectedUsers.get(i));
        }

        ResponseEntity<List<UserDTO>> response = userScoresPollaService.getUsersByPollaId(pollaId);
        List<UserDTO> result = response.getBody();

        assertNotNull(result);
        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers, result);

        verify(userScoresPollaRepository, times(1)).findByPollaId(pollaId);
        verify(userMapper, times(userScoresPollas.size())).toDTO(any(User.class));

    }

    @Test
    public void testFindByUserIdAndPollaId() throws Exception {
        Long userId = 1L;
        Long pollaId = 1L;
        UserScoresPolla userScoresPolla = UserScoresPollaUtil.userScoresPolla();
        UserScoresPollaDTO expectedDTO = UserScoresPollaDTO.builder()
                .userId(userScoresPolla.getUser().getId())
                .pollaId(userScoresPolla.getPolla().getId())
                .scores(userScoresPolla.getScores())
                .build();

        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(userId, pollaId))
                .thenReturn(Optional.of(userScoresPolla));

        when(userScoresPollaMapper.toDTO(userScoresPolla)).thenReturn(expectedDTO);

        ResponseEntity<UserScoresPollaDTO> response = userScoresPollaService.findByUserIdAndPollaId(userId, pollaId);
        UserScoresPollaDTO result = response.getBody();

        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(expectedDTO, result, "El DTO debe coincidir con el esperado");

        verify(userScoresPollaRepository, times(1)).findByUser_IdAndPolla_Id(userId, pollaId);
        verify(userScoresPollaMapper, times(1)).toDTO(userScoresPolla);
    }


    @Test
    public void testBanUserFromPolla() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 1L;
        UserScoresPolla userScoresPolla = UserScoresPollaUtil.userScoresPolla();

        assertNotNull(userScoresPolla, "El objeto userScoresPolla no debe ser nulo en el test");

        // Mockear el repositorio para encontrar el usuario
        when(userScoresPollaRepository.findByUser_IdAndPolla_Id(userId, pollaId))
                .thenReturn(Optional.of(userScoresPolla));

        // Capturar el objeto guardado para validar que se modificó correctamente
        ArgumentCaptor<UserScoresPolla> captor = ArgumentCaptor.forClass(UserScoresPolla.class);

        // Mockear el guardado
        when(userScoresPollaRepository.save(any(UserScoresPolla.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Devolver el objeto actualizado

        // Act
        ResponseEntity<Boolean> response = userScoresPollaService.banUserFromPolla(userId, pollaId);
        Boolean result = response.getBody();

        // Assert
        assertNotNull(result, "El resultado no debe ser nulo");
        assertTrue(result, "El resultado debe ser verdadero");

        // Verificar que el estado cambió antes de guardarlo
        verify(userScoresPollaRepository).save(captor.capture());
        UserScoresPolla savedUserScoresPolla = captor.getValue();
        assertEquals(UserPollaState.BLOQUEADO, savedUserScoresPolla.getState(), "El usuario debe estar bloqueado");

        // Verify
        verify(userScoresPollaRepository, times(1)).findByUser_IdAndPolla_Id(userId, pollaId);
        verify(userScoresPollaRepository, times(1)).save(any(UserScoresPolla.class));
    }

    */


    @Test
    void testRankingByPolla_Success() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        UserScoresPolla userScoresPolla1 = mock(UserScoresPolla.class);
        UserScoresPolla userScoresPolla2 = mock(UserScoresPolla.class);

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        UserDTO userDTO1 = UserUtil.userDTO();
        UserDTO userDTO2 = UserUtil.userDTO2();

        when(userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable))
                .thenReturn(List.of(userScoresPolla1, userScoresPolla2));
        when(userScoresPolla1.getUser()).thenReturn(user1);
        when(userScoresPolla2.getUser()).thenReturn(user2);
        when(userMapper.toDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toDTO(user2)).thenReturn(userDTO2);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(PollaUtil.polla()));


        // Act
        ResponseEntity<List<RankingDTO>> response = userScoresPollaService.rankingByPolla(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());


        verify(userScoresPollaRepository, times(1)).findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable);
        verify(userMapper, times(1)).toDTO(user1);
        verify(userMapper, times(1)).toDTO(user2);
    }

    @Test
    void testRankingByPolla_IllegalArgumentException() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable))
                .thenThrow(IllegalArgumentException.class);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(PollaUtil.polla()));

        // Act
        ResponseEntity<List<RankingDTO>> response = userScoresPollaService.rankingByPolla(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(userScoresPollaRepository, times(1)).findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable);
    }

    @Test
    void testRankingByPolla_RuntimeException() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable))
                .thenThrow(RuntimeException.class);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(PollaUtil.polla()));

        // Act
        ResponseEntity<List<RankingDTO>> response = userScoresPollaService.rankingByPolla(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(userScoresPollaRepository, times(1)).findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable);
    }

    @Test
    void testRankingBySubPolla_Success() {
        // Arrange
        Long subPollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        UserScoresPolla userScoresPolla1 = mock(UserScoresPolla.class);
        UserScoresPolla userScoresPolla2 = mock(UserScoresPolla.class);

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        UserDTO userDTO1 = UserUtil.userDTO();
        UserDTO userDTO2 = UserUtil.userDTO2();
        when(userScoresPollaRepository.rankingBySubPolla(subPollaId, pageable))
                .thenReturn(List.of(userScoresPolla1, userScoresPolla2));
        when(userScoresPolla1.getUser()).thenReturn(user1);
        when(userScoresPolla2.getUser()).thenReturn(user2);
        when(userMapper.toDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toDTO(user2)).thenReturn(userDTO2);
        when(subPollaRepository.findById(subPollaId)).thenReturn(Optional.of(SubPollaUtil.createSampleSubPollas().get(0)));

        // Act
        ResponseEntity<List<RankingDTO>> response = userScoresPollaService.rankingBySubPolla(subPollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());

        verify(userScoresPollaRepository, times(1)).rankingBySubPolla(subPollaId, pageable);
        verify(userMapper, times(1)).toDTO(user1);
        verify(userMapper, times(1)).toDTO(user2);
    }
    @Test
    void testFindRankingByUserIdAndPollaId_Success() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 1L;
        Long expectedRank = 5L;

        when(userScoresPollaRepository.findRankingByUserIdAndPollaId(userId, pollaId))
                .thenReturn(Optional.of(expectedRank));
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(PollaUtil.polla()));
        // Act
        ResponseEntity<Long> response = userScoresPollaService.findRankingByUserIdAndPollaId(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        verify(userScoresPollaRepository, times(1)).findRankingByUserIdAndPollaId(userId, pollaId);
    }

    @Test
    void testFindRankingByUserIdAndPollaId_UserNotInPolla() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 1L;

        when(userScoresPollaRepository.findRankingByUserIdAndPollaId(userId, pollaId))
                .thenReturn(Optional.empty());
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(PollaUtil.polla()));
        // Act & Assert
        assertThrows(UserNotInPollaException.class, () -> userScoresPollaService.findRankingByUserIdAndPollaId(userId, pollaId));

        verify(userScoresPollaRepository, times(1)).findRankingByUserIdAndPollaId(userId, pollaId);
    }

    @Test
    void testFindRankingSubPolla_UserNotInPolla() {
        // Arrange
        Long userId = 1L;
        Long subPollaId = 1L;

        when(userScoresPollaRepository.findRankingSubPolla(userId, subPollaId))
                .thenReturn(Optional.empty());
        when(subPollaRepository.findById(subPollaId)).thenReturn(Optional.of(SubPollaUtil.createSampleSubPollas().get(0)));
        // Act & Assert
        assertThrows(UserNotInPollaException.class, () -> userScoresPollaService.findRankingSubPolla(userId, subPollaId));

        verify(userScoresPollaRepository, times(1)).findRankingSubPolla(userId, subPollaId);
    }


}
