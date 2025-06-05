package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.RankingDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserScoresPolla API", description = "API for managing user scores in pollas")
@RequestMapping("/userscorespolla")
public interface UserScoresPollaAPI {

    @Operation(summary = "Update user scores by polla ID", description = "Updates the scores of all users in a specific polla")
    @PutMapping("/update-scores/{pollaId}")
    ResponseEntity<Boolean> updateUserScoresByPolla(@PathVariable Long pollaId);

    @Operation(summary = "Update user scores by match ID", description = "Updates the scores of all users in a specific match")
    @PutMapping("/update-scores/match/{matchId}")
    ResponseEntity<Boolean> updateUserScoresByMatch(@PathVariable Long matchId);

    @Operation(summary = "Get polla id by user id")
    @GetMapping("/user/{userId}/pollas")
    ResponseEntity<List<Long>> getPollaIdsByUserId(@PathVariable Long userId);

    @Operation(summary = "Get pollas by user id")
    @GetMapping("/pollas")
    ResponseEntity<List<PollaGetDTO>> getPollaByUserId(@RequestHeader("Authorization") String token);

    @Operation(summary = "Create User Scores Polla Relation")
    @PostMapping("/create/{userId}/{pollaId}")
    ResponseEntity<Boolean> createRelation(@PathVariable Long userId, @PathVariable Long pollaId);

    @Operation(summary = "Get ranking of users in a specific polla", description = "Returns a paginated list of users ranked by their scores in the specified polla")
    @GetMapping("/polla/{pollaId}")
    ResponseEntity<List<RankingDTO>> getRankingPolla(@PathVariable Long pollaId,
                                                  @PageableDefault(size = 10) Pageable pageable);

    @Operation(summary = "Get ranking of users in a specific sub-polla", description = "Returns a paginated list of users ranked by their scores in the specified sub-polla")
    @GetMapping("/subpolla/{subPollaId}")
    ResponseEntity<List<RankingDTO>> getRankingSubPolla(@PathVariable Long subPollaId,
                                                        @PageableDefault(size = 10) Pageable pageable);

    @Operation(summary = "Get users by polla ID", description = "Returns a list of users associated with a specific polla")
    @GetMapping("/position/polla/{pollaId}/users/{userId}")
    ResponseEntity<Long> findRankingByUserIdAndPollaId(@PathVariable Long userId, @PathVariable Long pollaId);

    @Operation(summary = "Get users by sub-polla ID", description = "Returns a list of users associated with a specific sub-polla")
    @GetMapping("/position/subpolla/{subPollaId}/users/{userId}")
    ResponseEntity<Long> findRankingSubPolla(@PathVariable Long userId, @PathVariable Long subPollaId);
}
