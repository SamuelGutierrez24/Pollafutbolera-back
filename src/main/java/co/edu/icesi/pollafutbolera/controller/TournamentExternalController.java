package co.edu.icesi.pollafutbolera.controller;

import java.util.List;

import co.edu.icesi.pollafutbolera.api.TournamentExternalAPI;
import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.service.TournamentExternalService;
import co.edu.icesi.pollafutbolera.service.TournamentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Tag(name = "Tournament API", description = "API for managing external tournaments")
public class TournamentExternalController implements TournamentExternalAPI {

    private final TournamentExternalService tournamentExternalService;
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @Override
    @Operation(summary = "Get all tournaments", description = "Retrieve all tournaments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of tournaments"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Mono<ResponseEntity<List<TournamentExternalDTO.LeagueData>>> getAllExternalLeagues() {
        return tournamentExternalService.getAllExternalLeagues();
    }

    @Override
    @Operation(summary = "Get an external tournament by ID", description = "Retrieve a tournament by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of tournament"),
                    @ApiResponse(responseCode = "404", description = "Tournament not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Mono<ResponseEntity<TournamentExternalDTO>> getExternalLeagueById(Long leagueId, int season) {
        return tournamentExternalService.getExternalLeagueById(leagueId, season);
    }

    @Override
    @Operation(summary = "Save a new external tournament", description = "Save a new external tournament",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful creation of tournament"),
                    @ApiResponse(responseCode = "404", description = "Tournament not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public Mono<ResponseEntity<TournamentDTO>> saveExternalLeague(Long leagueId, int season) {
        return tournamentExternalService.saveExternalLeague(leagueId, season);
    }
}