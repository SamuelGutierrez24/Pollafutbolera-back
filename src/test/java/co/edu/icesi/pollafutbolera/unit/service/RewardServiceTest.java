package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;
import co.edu.icesi.pollafutbolera.model.Reward;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.RewardRepository;
import co.edu.icesi.pollafutbolera.service.RewardServiceImpl;
import co.edu.icesi.pollafutbolera.util.RewardUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class  RewardServiceTest {

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private RewardMapper rewardMapper;

    @Mock
    private PollaRepository pollaRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Long rewardId = 1L;
        Reward reward = RewardUtil.reward();
        RewardDTO rewardDTO = new RewardDTO(rewardId, "Sample Reward", "Description", "Image", 1, 5L);

        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(rewardMapper.toRewardDTO(reward)).thenReturn(rewardDTO);

        RewardDTO response = rewardService.findById(rewardId).getBody();
        assertNotNull(response);
        assertEquals(rewardId, response.id());
        verify(rewardRepository, times(1)).findById(rewardId);
    }

    @Test
    public void testSaveAll() {
        List<RewardSaveDTO> rewardDTOs = RewardUtil.rewardSaveDTOs();
        Reward[] rewards = RewardUtil.rewards();

        when(rewardMapper.toRewardsFromSaveDTO(rewardDTOs.toArray(new RewardSaveDTO[0]))).thenReturn(rewards);
        when(pollaRepository.findById(rewards[0].getPolla().getId())).thenReturn(Optional.of(rewards[0].getPolla()));
        when(rewardRepository.saveAll(Arrays.asList(rewards))).thenReturn(Arrays.asList(rewards));
        rewardService.saveAll(rewardDTOs);

        verify(rewardRepository, times(1)).saveAll(Arrays.asList(rewards));
    }

    @Test
    public void testFindByPollaId() {
        Long pollaId = 1L;
        List<Reward> rewards = RewardUtil.rewardsList();
        RewardDTO[] rewardDTOs = RewardUtil.rewardDTOs().toArray(new RewardDTO[0]);

        when(rewardRepository.findByPollaId(pollaId)).thenReturn(Optional.of(rewards));
        when(rewardMapper.toRewardDTOs(rewards.toArray(new Reward[0]))).thenReturn(rewardDTOs);

        RewardDTO[] response = rewardService.findByPollaId(pollaId).getBody();
        assertNotNull(response);
        assertEquals(rewardDTOs.length, response.length);
        verify(rewardRepository, times(1)).findByPollaId(pollaId);
    }
}