package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.TournamentBetAPI;
import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.service.TournamentBetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournamentbets")
@AllArgsConstructor
@Tag(name = "TournamentBet API", description = "API for managing tournament bets")
public class TournamentBetController implements TournamentBetAPI {

    private final TournamentBetService tournamentBetService;

    @Override
    @Operation(summary = "Get a tournament bet by ID", description = "Retrieve a tournament bet by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournament bet found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentBetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tournament bet not found")
            })
    public ResponseEntity<TournamentBetDTO> getTournamentBet(@PathVariable Long id) {
        return tournamentBetService.getTournamentBetById(id);
    }

    @Override
    @Operation(summary = "Create a new tournament bet", description = "Create a new tournament bet",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tournament bet created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentBetDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "Conflict")
            })
    public ResponseEntity<TournamentBetDTO> createTournamentBet(@Valid @RequestBody TournamentBetDTO tournamentBetDTO) {
        return tournamentBetService.createTournamentBet(tournamentBetDTO);
    }

    @Override
    @Operation(summary = "Update an existing tournament bet", description = "Update an existing tournament bet",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournament bet updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentBetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tournament bet not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<TournamentBetDTO> updateTournamentBet(@Valid @PathVariable Long id, @Valid @RequestBody TournamentBetUpdateDTO updateTournamentBetDTO) {
        return tournamentBetService.updateTournamentBet(id, updateTournamentBetDTO);
    }

    @Override
    @Operation(summary = "Delete a tournament bet by ID", description = "Delete a tournament bet by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tournament bet deleted"),
                    @ApiResponse(responseCode = "404", description = "Tournament bet not found")
            })
    public ResponseEntity<Void> deleteTournamentBet(@PathVariable Long id) {
        return tournamentBetService.deleteTournamentBet(id);
    }

    @Override
    @Operation(summary = "Get a tournament bet by user ID and polla ID", description = "Retrieve a tournament bet by user ID and polla ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournament bet retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentBetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tournament bet not found")
            })
    public ResponseEntity<TournamentBetDTO> getTournamentBetByUserAndPolla(@PathVariable Long userId, @PathVariable Long pollaId) {
        return tournamentBetService.getTournamentBetByUserAndPolla(userId, pollaId);
    }
}