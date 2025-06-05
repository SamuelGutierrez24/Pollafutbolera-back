package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tournament API", description = "API for managing local tournaments")
@RequestMapping("/tournaments")
public interface TournamentAPI {

    @GetMapping("/register/{leagueId}/{season}")
    ResponseEntity<TournamentDTO> register(@PathVariable Long leagueId, @PathVariable int season);

    @Operation(summary = "Get all tournaments, returns a list of TournamentDTO")
    @GetMapping
    ResponseEntity<List<TournamentDTO>> getAllTournaments();

    @Operation(summary = "Get a tournaments by ID, returns a TournamentDTO")
    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentDTO> getTournamentById(@PathVariable("id") Long id);

    @Operation(summary = "Save a new manual tournament, requires a TournamentDTO composed of a name, a description and an unique ID")
    @PostMapping
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO tournamentDTO);

    @Operation(summary = "Update an existing tournament, requires an ID and a TournamentDTO composed of a name, a description, a winner_team_id, a fewest_goals_conceded_team_id and a top_scoring_team_id")
    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentDTO> updateTournament(@PathVariable("id") Long id, @RequestBody TournamentDTO tournamentDTO);

    @Operation(summary = "Delete a tournament by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<Void> deleteTournament(@PathVariable("id") Long id);

    @GetMapping("/statistics")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentStatistics> getStatistics(@RequestParam Long tournamentId);

    @GetMapping("/tournament")
    @PreAuthorize("permitAll()")
    ResponseEntity<Tournament> getTournament(@RequestParam Long tournamentId);

    @GetMapping("/stats")
    @PreAuthorize("permitAll()")
    ResponseEntity<TournamentStatsDTO> updateTournamentStats(@RequestParam Long tournamentId);
}
