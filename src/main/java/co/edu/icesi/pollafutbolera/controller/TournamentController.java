package co.edu.icesi.pollafutbolera.controller;

import java.util.List;

import co.edu.icesi.pollafutbolera.client.FootballApiClient;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.service.FootballService;
import co.edu.icesi.pollafutbolera.service.StageService;
import co.edu.icesi.pollafutbolera.api.TournamentAPI;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import co.edu.icesi.pollafutbolera.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.icesi.pollafutbolera.api.TournamentAPI;

import java.util.List;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.service.TournamentService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import co.edu.icesi.pollafutbolera.service.TournamentRegistrationService;

@RestController
@AllArgsConstructor
@Tag(name = "Tournament API", description = "API for managing local tournaments")
public class TournamentController implements TournamentAPI {

    private final TournamentService tournamentService;
    private final StageService stageService;
    private final TournamentMapper tournamentMapper;
    private final TournamentRegistrationService tournamentRegistrationService;


    @Override
    public ResponseEntity<TournamentDTO> register(Long leagueId, int season) {
        try {
            return tournamentRegistrationService.registerTournament(leagueId, season);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @Override
    @Operation(summary = "Get all tournaments", description = "Retrieve all tournaments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of tournaments",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentDTO[].class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @Override
    @Operation(summary = "Get a tournament by ID", description = "Retrieve a tournament by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of tournament",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tournament not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TournamentDTO> getTournamentById(Long id) {
        return tournamentService.getTournamentById(id);
    }

    @Override
    @Operation(summary = "Save a new manual tournament", description = "Save a new manual tournament",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful creation of tournament",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO) {
        return tournamentService.createTournament(tournamentDTO);
    }

    @Override
    @Operation(summary = "Update an existing tournament", description = "Update an existing tournament",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful update of tournament",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Tournament not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TournamentDTO> updateTournament(Long id, TournamentDTO tournamentDTO) {
        return tournamentService.updateTournament(id, tournamentDTO);
    }

    @Override
    @Operation(summary = "Delete a tournament by ID", description = "Delete a tournament by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful deletion of tournament"),
                    @ApiResponse(responseCode = "404", description = "Tournament not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> deleteTournament(Long id) {
        return tournamentService.deleteTournament(id);
    }

    @Override
    @Operation(summary = "Get Standings",
            description = "Retrieves the standings for a given league and season",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Standings retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            })
    public ResponseEntity<TournamentStatistics> getStatistics(@RequestParam Long tournamentId) {
        return tournamentService.getTournamentStatistics(tournamentId);
    }

    @Override
    @Operation(
            summary = "Get Tournament by ID",
            description = "Retrieves the tournament details for the given tournament ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tournament retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Tournament.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tournament not found",
                            content = @io.swagger.v3.oas.annotations.media.Content
                    )
            }
    )
    public ResponseEntity<Tournament> getTournament(@RequestParam Long tournamentId) {
        return tournamentService.getTournament(tournamentId);
    }


    @Override
    @Operation(summary = "Update Tournament Stats",
            description = "Updates tournament statistics based on external API data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournament statistics updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TournamentStatsDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TournamentStatsDTO> updateTournamentStats(@RequestParam Long tournamentId) {
        return tournamentService.updateTournamentStats(tournamentId);
    }
}