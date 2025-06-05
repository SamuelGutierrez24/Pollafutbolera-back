package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.TeamApi;
import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import co.edu.icesi.pollafutbolera.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@AllArgsConstructor
@Tag(name = "Team API", description = "API for managing teams")
public class TeamRestController implements TeamApi {

    private final TeamService teamService;

    @Operation(summary   = "Find all teams",
            description = "Returns the complete list of teams")
    @ApiResponse(responseCode = "200",
            description   = "Teams retrieved",
            content       = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDTO.class)))
    @Override
    @GetMapping
    public ResponseEntity<List<TeamDTO>> findAll() {
        try {
            return teamService.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary   = "Create team",
            description = "Creates a new team")
    @ApiResponse(responseCode = "201",
            description   = "Team created",
            content       = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDTO.class)))
    @Override
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        try {
            return teamService.createTeam(teamDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary   = "Find team by ID",
            description = "Returns the team with the given ID")
    @ApiResponse(responseCode = "200",
            description   = "Team found",
            content       = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDTO.class)))
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findById(@PathVariable Long id) {
        try {
            return teamService.findById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary   = "Update team",
            description = "Updates the team with the given ID")
    @ApiResponse(responseCode = "200",
            description   = "Team updated",
            content       = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDTO.class)))
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(
            @PathVariable Long id,
            @RequestBody TeamDTO teamDTO) {
        try {
            return teamService.save(id, teamDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary   = "Delete team",
            description = "Deletes the team with the given ID")
    @ApiResponse(responseCode = "204", description = "Team deleted")
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            teamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary   = "Fetch teams from external API",
            description = "Imports every team for the specified league/season and stores or updates them")
    @ApiResponse(responseCode = "204", description = "Teams imported successfully")
    @PostMapping("/save")
    public ResponseEntity<Void> fetchAndSave(
            @RequestParam Integer league,
            @RequestParam Integer season) {

        try {
            return teamService.fetchAndSaveTeams(league, season);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary   = "Fetch teams",
            description = "Imports every team for the specified tournament")
    @ApiResponse(responseCode = "204", description = "Teams imported successfully")
    @GetMapping("/fetch")
    public ResponseEntity<List<TeamDTO>> fetchByLeagueAndSeason(
            @RequestParam("league") Integer league,
            @RequestParam("season") Integer season) {
        return teamService.fetchTeamsByLeagueAndSeason(league, season);
    }

    @Operation(summary   = "Get teams",
            description = "Imports every team for the specified tournament in db")
    @ApiResponse(responseCode = "204", description = "Teams imported successfully")
    @GetMapping("/tournament")
    public ResponseEntity<List<TeamDTO>> getTeamsInTournament(
            @RequestParam("tournament") Long tournament) {
        return teamService.getTeamsInTournament(tournament);
    }
}
