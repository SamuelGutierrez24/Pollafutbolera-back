package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TournamentBet API", description = "API for managing tournament bets")
@RequestMapping("/tournamentbets")
public interface TournamentBetAPI {

    @Operation(summary = "Get a tournament bet by ID, returns a TournamentBetDTO")
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentBetDTO> getTournamentBet(@PathVariable Long id);

    @Operation(summary = "Create a new tournament bet, requires a TournamentBetDTO, returns the created TournamentBetDTO")
    @PostMapping
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentBetDTO> createTournamentBet(@RequestBody TournamentBetDTO tournamentBetDTO);

    @Operation(summary = "Update an existing tournament bet, requires an ID and a TournamentBetUpdateDTO, returns the updated TournamentBetDTO")
    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentBetDTO> updateTournamentBet(@PathVariable Long id, @RequestBody TournamentBetUpdateDTO updateTournamentBetDTO);

    @Operation(summary = "Delete a tournament bet by ID, returns void")
    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<Void> deleteTournamentBet(@PathVariable Long id);

    @Operation(summary = "Get a tournament bet by user ID and polla ID, returns a TournamentBetDTO")
    @GetMapping("/user/{userId}/polla/{pollaId}")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentBetDTO> getTournamentBetByUserAndPolla(@PathVariable Long userId, @PathVariable Long pollaId);
}