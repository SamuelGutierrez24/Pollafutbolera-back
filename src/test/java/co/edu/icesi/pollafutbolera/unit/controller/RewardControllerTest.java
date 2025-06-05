package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.RewardController;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.service.RewardService;
import co.edu.icesi.pollafutbolera.util.RewardUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardByPolla() {
        Long pollaId = 1L;
        RewardDTO[] rewardDTOs = RewardUtil.rewardDTOs().toArray(new RewardDTO[0]);

        when(rewardService.findByPollaId(pollaId)).thenReturn(ResponseEntity.ok(rewardDTOs));

        ResponseEntity<RewardDTO[]> response = rewardController.getRewardByPolla(pollaId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(rewardDTOs), response);
        verify(rewardService, times(1)).findByPollaId(pollaId);
    }

    @Test
    public void testSaveRewards() {
        RewardSaveDTO[] rewardDTOs = RewardUtil.rewardSaveDTOs().toArray(new RewardSaveDTO[0]);

        when(rewardService.saveAll(List.of(rewardDTOs))).thenReturn(ResponseEntity.ok(RewardUtil.rewardDTOs().toArray(new RewardDTO[0])));

        rewardController.saveRewards(rewardDTOs);

        verify(rewardService, times(1)).saveAll(List.of(rewardDTOs));
    }
}