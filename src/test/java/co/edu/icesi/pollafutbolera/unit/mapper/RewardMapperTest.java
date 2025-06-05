package co.edu.icesi.pollafutbolera.unit.mapper;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;
import co.edu.icesi.pollafutbolera.model.Reward;
import co.edu.icesi.pollafutbolera.util.RewardUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class RewardMapperTest {

    RewardMapper rewardMapper = Mappers.getMapper(RewardMapper.class);

    @Test
    public void testToRewardDTO() {
        Reward reward = RewardUtil.reward();

        RewardDTO rewardDTO = rewardMapper.toRewardDTO(reward);

        assertNotNull(rewardDTO);
        assertEquals(reward.getId(), rewardDTO.id());
        assertEquals(reward.getDescription(), rewardDTO.description());
        assertEquals(reward.getName(), rewardDTO.name());
        assertEquals(reward.getImage(), rewardDTO.image());
        assertEquals(reward.getPosition(), rewardDTO.position());
    }

    @Test
    public void testToRewardDTOs() {
        Reward[] rewards = RewardUtil.rewards();

        RewardDTO[] rewardDTOs = rewardMapper.toRewardDTOs(rewards);

        assertNotNull(rewardDTOs);
        assertEquals(rewards.length, rewardDTOs.length);
        for (int i = 0; i < rewards.length; i++) {
            assertEquals(rewards[i].getId(), rewardDTOs[i].id());
            assertEquals(rewards[i].getDescription(), rewardDTOs[i].description());
            assertEquals(rewards[i].getName(), rewardDTOs[i].name());
            assertEquals(rewards[i].getImage(), rewardDTOs[i].image());
            assertEquals(rewards[i].getPosition(), rewardDTOs[i].position());
        }
    }

    @Test
    public void testToReward() {
        RewardDTO rewardDTO = RewardUtil.rewardDTO();

        Reward reward = rewardMapper.toReward(rewardDTO);

        assertNotNull(reward);
        assertEquals(rewardDTO.description(), reward.getDescription());
        assertEquals(rewardDTO.name(), reward.getName());
        assertEquals(rewardDTO.image(), reward.getImage());
        assertEquals(rewardDTO.position(), reward.getPosition());
        assertEquals(rewardDTO.pollaId(), reward.getPolla().getId());
    }

    @Test
    public void testToRewards() {
        RewardDTO[] rewardDTOs = RewardUtil.rewardDTOs().toArray(new RewardDTO[0]);

        Reward[] rewards = rewardMapper.toRewards(rewardDTOs);

        assertNotNull(rewards);
        assertEquals(rewardDTOs.length, rewards.length);
        for (int i = 0; i < rewardDTOs.length; i++) {
            assertEquals(rewardDTOs[i].description(), rewards[i].getDescription());
            assertEquals(rewardDTOs[i].name(), rewards[i].getName());
            assertEquals(rewardDTOs[i].image(), rewards[i].getImage());
            assertEquals(rewardDTOs[i].position(), rewards[i].getPosition());
            assertEquals(rewardDTOs[i].pollaId(), rewards[i].getPolla().getId());
        }
    }

    @Test
    public void testToRewardDTONull() {
        Reward reward = null;

        RewardDTO rewardDTO = rewardMapper.toRewardDTO(reward);

        assertNull(rewardDTO);
    }

    @Test
    public void testToRewardNull() {
        RewardDTO rewardDTO = null;

        Reward reward = rewardMapper.toReward(rewardDTO);

        assertNull(reward);
    }

    @Test
    public void testToRewardDTOInvalidData() {
        Reward reward = new Reward();
        reward.setId(1L);
        reward.setDescription(null); // Invalid data
        reward.setName(null); // Invalid data
        reward.setImage(null); // Invalid data
        reward.setPosition(null); // Invalid data

        RewardDTO rewardDTO = rewardMapper.toRewardDTO(reward);

        assertNotNull(rewardDTO);
        assertNull(rewardDTO.description());
        assertNull(rewardDTO.name());
        assertNull(rewardDTO.image());
        assertNull(rewardDTO.position());
    }

    @Test
    public void testToRewardInvalidData() {
        RewardDTO rewardSaveDTO = new RewardDTO(
                null, // Invalid data
                null,// Invalid data
                null, // Invalid data
                null, // Invalid data
                null, // Invalid data
                1L
        );

        Reward reward = rewardMapper.toReward(rewardSaveDTO);

        assertNotNull(reward);
        assertNull(reward.getDescription());
        assertNull(reward.getName());
        assertNull(reward.getImage());
        assertNull(reward.getPosition());
    }

    @Test
    public void testToRewardsFromSaveDTO() {
        RewardSaveDTO[] rewardSaveDTOs = RewardUtil.rewardSaveDTOs().toArray(new RewardSaveDTO[0]);

        Reward[] rewards = rewardMapper.toRewardsFromSaveDTO(rewardSaveDTOs);

        assertNotNull(rewards);
        assertEquals(rewardSaveDTOs.length, rewards.length);
        for (int i = 0; i < rewardSaveDTOs.length; i++) {
            assertEquals(rewardSaveDTOs[i].name(), rewards[i].getName());
            assertEquals(rewardSaveDTOs[i].description(), rewards[i].getDescription());
            assertEquals(rewardSaveDTOs[i].image(), rewards[i].getImage());
            assertEquals(rewardSaveDTOs[i].position(), rewards[i].getPosition());
            assertEquals(rewardSaveDTOs[i].pollaId(), rewards[i].getPolla().getId());
        }
    }

}