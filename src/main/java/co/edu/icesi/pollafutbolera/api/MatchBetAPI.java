package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MatchBet API", description = "API for managing match bets")
@RequestMapping("/matchbets")
public interface MatchBetAPI {

    @Operation(summary = "Get a match bet by ID, returns a MatchBetResponseDTO")
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<MatchBetResponseDTO> getMatchBet(@PathVariable Long id);

    @Operation(summary = "Create a new match bet, requires a MatchBetDTO, returns the created MatchBetResponseDTO")
    @PostMapping
    @PreAuthorize("permitAll()")
    ResponseEntity<MatchBetResponseDTO> createMatchBet(@RequestBody MatchBetDTO matchBetDTO);

    @Operation(summary = "Update an existing match bet, requires an ID and a MatchBetUpdateDTO, returns the updated MatchBetResponseDTO")
    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<MatchBetResponseDTO> updateMatchBet(@PathVariable Long id, @RequestBody MatchBetUpdateDTO updateMatchBetDTO);

    @Operation(summary = "Delete a match bet by ID, returns void")
    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<Void> deleteMatchBet(@PathVariable Long id);

    @Operation(summary = "Get all match bets by user ID, returns a list of MatchBetResponseDTO")
    @GetMapping("/user/{userId}")
    @PreAuthorize("permitAll()")
    ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUser(@PathVariable Long userId);

    @Operation(summary = "Get all match bets by user ID and polla ID, returns a list of MatchBetResponseDTO")
    @GetMapping("/user/{userId}/polla/{pollaId}")
    @PreAuthorize("permitAll()")
    ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUserAndPolla(@PathVariable Long userId, @PathVariable Long pollaId);
}
