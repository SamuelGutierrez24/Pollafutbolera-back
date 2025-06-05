package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reward API", description = "API for managing rewards, a reward is a prize that can be won by a user in a polla")
@RequestMapping("/reward")
public interface RewardAPI {

    @Operation(summary = "Get rewards by polla ID, returns an array of RewardDTO")
    @GetMapping(value ="/polla/{pollaId}", produces = "application/json")
    ResponseEntity<RewardDTO[]> getRewardByPolla(@PathVariable Long pollaId);

    @Operation(summary = "Save rewards, requires an array of RewardDTO, RewardDTO has a name, a description, a link to an image, a numeric position, and the Id of a polla, returns void")
    @PostMapping("/save")
    ResponseEntity<RewardDTO[]> saveRewards(@RequestBody RewardSaveDTO[] rewards);

}