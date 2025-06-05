package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.MatchBetAPI;
import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.service.MatchBetService;
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

import java.util.List;

@RestController
@RequestMapping("/matchbets")
@AllArgsConstructor
@Tag(name = "MatchBet API", description = "API for managing match bets")
public class MatchBetController implements MatchBetAPI {

    private final MatchBetService matchBetService;

    @Override
    @Operation(summary = "Get a match bet by ID", description = "Retrieve a match bet by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match bet found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchBetResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Match bet not found")
            })
    public ResponseEntity<MatchBetResponseDTO> getMatchBet(@PathVariable Long id) {
        return matchBetService.getMatchBetById(id);
    }

    @Override
    @Operation(summary = "Create a new match bet", description = "Create a new match bet",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Match bet created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchBetResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "Conflict")
            })
    public ResponseEntity<MatchBetResponseDTO> createMatchBet(@Valid @RequestBody MatchBetDTO matchBetDTO) {
        return matchBetService.createMatchBet(matchBetDTO);
    }

    @Override
    @Operation(summary = "Get all match bets by user ID", description = "Retrieve all match bets by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match bets retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchBetResponseDTO.class)))
            })
    public ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUser(@PathVariable Long userId) {
        return matchBetService.getMatchBetsByUser(userId);
    }

    @Override
    @Operation(summary = "Get all match bets by user ID and polla ID", description = "Retrieve all match bets by user ID and polla ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match bets retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchBetResponseDTO.class)))
            })
    public ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUserAndPolla(@PathVariable Long userId, @PathVariable Long pollaId) {
        return matchBetService.getMatchBetsByUserAndPolla(userId, pollaId);
    }

    @Override
    @Operation(summary = "Update an existing match bet", description = "Update an existing match bet",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match bet updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchBetResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Match bet not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "Conflict")
            })
    public ResponseEntity<MatchBetResponseDTO> updateMatchBet(@Valid @PathVariable Long id, @Valid @RequestBody MatchBetUpdateDTO updateMatchBetDTO) {
        return matchBetService.updateMatchBet(id, updateMatchBetDTO);
    }

    @Override
    @Operation(summary = "Delete a match bet by ID", description = "Delete a match bet by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Match bet deleted"),
                    @ApiResponse(responseCode = "404", description = "Match bet not found")
            })
    public ResponseEntity<Void> deleteMatchBet(@PathVariable Long id) {
        return matchBetService.deleteMatchBet(id);
    }
}
