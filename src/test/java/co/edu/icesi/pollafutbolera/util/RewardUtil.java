package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.model.Reward;

import java.util.Arrays;
import java.util.List;

public class RewardUtil {
    public static Reward reward() {
        return Reward.builder()
                .id(1L)
                .description("desc")
                .name("reward")
                .image("image")
                .position(1)
                .polla(PollaUtil.polla())
                .build();
    }

    public static RewardDTO rewardDTO() {
        return RewardDTO.builder()
                .name("reward")
                .description("desc")
                .image("image")
                .position(1)
                .pollaId(10L)
                .build();
    }

    public static List<RewardDTO> rewardDTOs() {
        return Arrays.asList(
                RewardDTO.builder()
                        .id(10L)
                        .name("reward1")
                        .description("desc1")
                        .image("image1")
                        .position(1)
                        .pollaId(11L)
                        .build(),
                RewardDTO.builder()
                        .id(11L)
                        .name("reward2")
                        .description("desc2")
                        .image("image2")
                        .position(2)
                        .pollaId(11L)
                        .build()
        );
    }

    public static List<RewardSaveDTO> rewardSaveDTOs() {
        return Arrays.asList(
                RewardSaveDTO.builder()
                        .name("reward1")
                        .description("desc1")
                        .image("image1")
                        .position(1)
                        .pollaId(11L)
                        .build(),
                RewardSaveDTO.builder()
                        .name("reward2")
                        .description("desc2")
                        .image("image2")
                        .position(2)
                        .pollaId(11L)
                        .build()
        );
    }

    public static Reward[] rewards() {
        return new Reward[]{
                Reward.builder()
                        .id(1L)
                        .description("desc1")
                        .name("reward1")
                        .image("image1")
                        .position(1)
                        .polla(PollaUtil.polla())
                        .build(),
                Reward.builder()
                        .id(2L)
                        .description("desc2")
                        .name("reward2")
                        .image("image2")
                        .position(2)
                        .polla(PollaUtil.polla())
                        .build()
        };
    }

    public static List<Reward> rewardsList() {
        return Arrays.asList(rewards());
    }

}