package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.UserScoresPollaController;
import co.edu.icesi.pollafutbolera.dto.RankingDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.service.UserScoresPollaService;
import co.edu.icesi.pollafutbolera.util.UserScoresPollaUtil;
import co.edu.icesi.pollafutbolera.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class UserScoresPollaControllerTest {

    @Mock
    private UserScoresPollaService userScoresPollaService;

    @InjectMocks
    private UserScoresPollaController userScoresPollaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateUserScoresByPolla() {
        Long pollaId = 1L;

        when(userScoresPollaService.updateUserScoresByPolla(pollaId)).thenReturn(ResponseEntity.ok(true));

        ResponseEntity<Boolean> response = userScoresPollaController.updateUserScoresByPolla(pollaId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(true), response);
        verify(userScoresPollaService, times(1)).updateUserScoresByPolla(pollaId);
    }

    @Test
    public void testGetRankingPolla() {
        Long pollaId = 1L;



        when(userScoresPollaService.rankingByPolla(pollaId, Pageable.ofSize(10))).thenReturn(ResponseEntity.ok(UserScoresPollaUtil.createRankingDTOs()));

        ResponseEntity<List<RankingDTO>> response = userScoresPollaController.getRankingPolla(pollaId, Pageable.ofSize(10));

        verify(userScoresPollaService, times(1)).rankingByPolla(pollaId, Pageable.ofSize(10));
    }

    @Test
    public void testGetRankingSubPolla() {
        // Arrange
        Long subPollaId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        List<RankingDTO> expectedUsers = UserScoresPollaUtil.createRankingDTOs();

        when(userScoresPollaService.rankingBySubPolla(subPollaId, pageable))
                .thenReturn(ResponseEntity.ok(expectedUsers));

        // Act
        ResponseEntity<List<RankingDTO>> response = userScoresPollaController.getRankingSubPolla(subPollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
        verify(userScoresPollaService, times(1)).rankingBySubPolla(subPollaId, pageable);
    }

    @Test
    public void testFindRankingByUserIdAndPollaId() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 2L;
        Long expectedRank = 5L;

        when(userScoresPollaService.findRankingByUserIdAndPollaId(userId, pollaId))
                .thenReturn(ResponseEntity.ok(expectedRank));

        // Act
        ResponseEntity<Long> response = userScoresPollaController.findRankingByUserIdAndPollaId(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedRank, response.getBody());
        verify(userScoresPollaService, times(1)).findRankingByUserIdAndPollaId(userId, pollaId);
    }

    @Test
    public void testFindRankingSubPolla() {
        // Arrange
        Long userId = 1L;
        Long subPollaId = 2L;
        Long expectedRank = 3L;

        when(userScoresPollaService.findRankingSubPolla(userId, subPollaId))
                .thenReturn(ResponseEntity.ok(expectedRank));

        // Act
        ResponseEntity<Long> response = userScoresPollaController.findRankingSubPolla(userId, subPollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(expectedRank, response.getBody());
        verify(userScoresPollaService, times(1)).findRankingSubPolla(userId, subPollaId);
    }



    @Test
    public void testUpdateUserScoresByMatch() {
        Long matchId = 1L;
        when(userScoresPollaService.updateUserScoresByMatch(matchId))
                .thenReturn(ResponseEntity.ok(true));

        ResponseEntity<Boolean> response = userScoresPollaController.updateUserScoresByMatch(matchId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(true), response);
        verify(userScoresPollaService, times(1)).updateUserScoresByMatch(matchId);
    }

}