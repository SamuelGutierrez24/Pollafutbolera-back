package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.PlatformConfigController;
import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.service.PlatformConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlatformConfigControllerTest {

    @Mock
    private PlatformConfigService platformConfigService;

    @InjectMocks
    private PlatformConfigController platformConfigController;

    private PlatformConfigDTO platformConfigDTO;
    private List<PlatformConfigDTO> platformConfigDTOList;

    @BeforeEach
    void setUp() {
        // Create test data
        platformConfigDTO = PlatformConfigDTO.builder()
                .tournamentChampion(10)
                .teamWithMostGoals(5)
                .exactScore(3)
                .matchWinner(1)
                .build();

        platformConfigDTOList = Arrays.asList(
                platformConfigDTO,
                PlatformConfigDTO.builder()
                        .tournamentChampion(15)
                        .teamWithMostGoals(7)
                        .exactScore(4)
                        .matchWinner(2)
                        .build()
        );
    }

    @Test
    void testGetPlatformConfigById_Success() {
        // Arrange
        Long configId = 1L;
        when(platformConfigService.getPlatformConfigById(configId)).thenReturn(ResponseEntity.ok(platformConfigDTO));

        // Act
        ResponseEntity<PlatformConfigDTO> response = platformConfigController.getPlatformConfigById(configId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(platformConfigDTO, response.getBody());
        verify(platformConfigService, times(1)).getPlatformConfigById(configId);
    }

    @Test
    void testGetAllPlatformConfigs_Success() {
        // Arrange
        when(platformConfigService.getAllPlatformConfigs()).thenReturn(ResponseEntity.ok(platformConfigDTOList));

        // Act
        ResponseEntity<List<PlatformConfigDTO>> response = platformConfigController.getAllPlatformConfigs();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(platformConfigDTOList, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(platformConfigService, times(1)).getAllPlatformConfigs();
    }
}