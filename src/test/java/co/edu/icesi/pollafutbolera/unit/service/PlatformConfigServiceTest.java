package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.mapper.PlatformConfigMapper;
import co.edu.icesi.pollafutbolera.model.PlatformConfig;
import co.edu.icesi.pollafutbolera.repository.PlatformConfigRepository;
import co.edu.icesi.pollafutbolera.service.PlatformConfigServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlatformConfigServiceTest {

    @Mock
    private PlatformConfigRepository platformConfigRepository;

    @Mock
    private PlatformConfigMapper platformConfigMapper;

    @InjectMocks
    private PlatformConfigServiceImpl platformConfigService;

    private PlatformConfig platformConfig;
    private PlatformConfigDTO platformConfigDTO;
    private List<PlatformConfig> platformConfigList;
    private List<PlatformConfigDTO> platformConfigDTOList;

    @BeforeEach
    void setUp() {
        // Create test entity
        platformConfig = PlatformConfig.builder()
                .id(1L)
                .tournamentChampion(10)
                .teamWithMostGoals(5)
                .exactScore(3)
                .matchWinner(1)
                .build();

        // Create test DTO
        platformConfigDTO = PlatformConfigDTO.builder()
                .tournamentChampion(10)
                .teamWithMostGoals(5)
                .exactScore(3)
                .matchWinner(1)
                .build();

        // Create test lists
        platformConfigList = Arrays.asList(
                platformConfig,
                PlatformConfig.builder()
                        .id(2L)
                        .tournamentChampion(15)
                        .teamWithMostGoals(7)
                        .exactScore(4)
                        .matchWinner(2)
                        .build()
        );

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
        when(platformConfigRepository.findById(configId)).thenReturn(Optional.of(platformConfig));
        when(platformConfigMapper.toDTO(platformConfig)).thenReturn(platformConfigDTO);

        // Act
        ResponseEntity<PlatformConfigDTO> response = platformConfigService.getPlatformConfigById(configId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(platformConfigDTO, response.getBody());
        verify(platformConfigRepository).findById(configId);
        verify(platformConfigMapper).toDTO(platformConfig);
    }

    @Test
    void testGetPlatformConfigById_NotFound() {
        // Arrange
        Long configId = 999L;
        when(platformConfigRepository.findById(configId)).thenReturn(Optional.empty());
        when(platformConfigMapper.toDTO(null)).thenReturn(null);

        // Act
        ResponseEntity<PlatformConfigDTO> response = platformConfigService.getPlatformConfigById(configId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(platformConfigRepository).findById(configId);
        verify(platformConfigMapper).toDTO(null);
    }

    @Test
    void testGetAllPlatformConfigs_Success() {
        // Arrange
        when(platformConfigRepository.findAll()).thenReturn(platformConfigList);
        when(platformConfigMapper.toDTOList(platformConfigList)).thenReturn(platformConfigDTOList);

        // Act
        ResponseEntity<List<PlatformConfigDTO>> response = platformConfigService.getAllPlatformConfigs();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(platformConfigDTOList, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(platformConfigRepository).findAll();
        verify(platformConfigMapper).toDTOList(platformConfigList);
    }

    @Test
    void testGetAllPlatformConfigs_EmptyList() {
        // Arrange
        when(platformConfigRepository.findAll()).thenReturn(List.of());
        when(platformConfigMapper.toDTOList(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<List<PlatformConfigDTO>> response = platformConfigService.getAllPlatformConfigs();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(platformConfigRepository).findAll();
        verify(platformConfigMapper).toDTOList(List.of());
    }
}
