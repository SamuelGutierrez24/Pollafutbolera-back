package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.RewardAPI;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
    import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Reward API", description = "API for managing rewards")
public class RewardController implements RewardAPI {

    private final RewardService rewardService;

    @Override
    @Operation(summary = "Get rewards by polla ID", description = "Retrieve rewards by polla ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of rewards",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RewardDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Rewards not found")
            })
    public ResponseEntity<RewardDTO[]> getRewardByPolla(Long pollaId) {
        return rewardService.findByPollaId(pollaId);
    }

    @Override
    @Operation(summary = "Save rewards", description = "Save multiple rewards",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful saving of rewards",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RewardDTO[].class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<RewardDTO[]> saveRewards(RewardSaveDTO[] rewards) {
        return rewardService.saveAll(List.of(rewards));
    }
}