package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Reward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PollaMapper.class})
public interface RewardMapper {

    RewardDTO toRewardDTO(Reward reward);

    @Mapping(target = "polla.id", source = "pollaId")
    Reward toReward(RewardDTO rewardDTO);

    @Mapping(target = "polla.id", source = "pollaId")
    Reward[] toRewards(RewardDTO[] rewardDTO);

    @Mapping(target = "polla.id", source = "pollaId")
    Reward toRewardFromSaveDTO(RewardSaveDTO rewardDTO);

    @Mapping(target = "polla.id", source = "pollaId")
    Reward[] toRewardsFromSaveDTO(RewardSaveDTO[] rewardDTO);

    @Mapping(source = "polla.id", target = "pollaId")
    RewardDTO[] toRewardDTOs(Reward[] reward);

    default Polla map(Long pollaId) {
        if (pollaId == null) {
            return null;
        }
        Polla polla = new Polla();
        polla.setId(pollaId);
        return polla;
    }
}
