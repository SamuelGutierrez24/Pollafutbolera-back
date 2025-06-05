package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Tournament API", description = "API for managing external tournaments")
@RequestMapping("/external/tournaments")
public interface TournamentExternalAPI {

    @Operation(summary = "Get all tournaments, returns a list of TournamentExternalDTO.LeagueData")
    @GetMapping
    Mono<ResponseEntity<List<TournamentExternalDTO.LeagueData>>> getAllExternalLeagues();

    @Operation(summary = "Get an external tournament by ID, returns a TournamentExternalDTO")
    @GetMapping(value = "/{leagueId}/{season}")
    @PreAuthorize("permitAll()")
    Mono<ResponseEntity<TournamentExternalDTO>> getExternalLeagueById(@PathVariable("leagueId") Long leagueId, @PathVariable("season") int season);

    @Operation(summary = "Save a new external tournament, requires the ID of the tournament in the FootballAPI, returns a TournamentDTO")
    @PostMapping("/{leagueId}/{season}/save")
    @PreAuthorize("permitAll()")
    Mono<ResponseEntity<TournamentDTO>> saveExternalLeague(@PathVariable("leagueId") Long leagueId, @PathVariable("season") int season);

}
