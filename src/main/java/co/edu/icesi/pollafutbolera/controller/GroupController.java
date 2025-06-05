package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.dto.GroupDTO;
import co.edu.icesi.pollafutbolera.service.GroupService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@AllArgsConstructor
@Tag(name = "Group API", description = "API for managing groups")
public class GroupController {

    private final GroupService groupService;

    
    
    
    @Operation(
            summary = "Retrieve raw standings JSON",
            description = "Retrieves raw standings data JSON from the external API (for debugging/test)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Standings JSON retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JsonNode.class)
            )
    )
    @GetMapping("/test/standings/raw")
    public List<GroupDTO> retrieveRawStandingsJson(
            @RequestParam("league") Integer league,
            @RequestParam("season") Integer season) {

        
        
        
        List<GroupDTO> json = groupService.retrieveStandingsAsDTOs(league, season).block();
        return json;
    }

    
    
    
    @Operation(
            summary = "Retrieve raw group stage fixtures JSON",
            description = "Retrieves raw group stage fixtures JSON from the external API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fixtures JSON retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JsonNode.class)
            )
    )
    @GetMapping("/test/fixtures/raw")
    public ResponseEntity<JsonNode> retrieveGroupStageFixtures(
        @RequestParam("league") Integer league,
        @RequestParam("season") Integer season) {

        try {
                JsonNode json = groupService.retrieveGroupStageFixtures(league, season).block();
                return ResponseEntity.ok(json);
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    
    
    @Operation(
            summary = "Retrieve and save standings",
            description = "Fetches standings from the external API and persists them in the DB"
    )
    @ApiResponse(responseCode = "204", description = "Standings retrieved and saved")
    @GetMapping("/standings/save")
    public ResponseEntity<Void> retrieveAndSaveStandings(
        @RequestParam("league") Integer league,
        @RequestParam("season") Integer season) {

        try {
                groupService.retrieveAndSaveStandings(league, season).block();
                return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    
    
    @Operation(
            summary = "Retrieve and save group stage matches",
            description = "Fetches group stage matches from the external API and persists them in the DB"
    )
    @ApiResponse(responseCode = "204", description = "Group stage matches retrieved and saved")
    @GetMapping("/matches/save")
    public ResponseEntity<Void> retrieveAndSaveGroupStageMatches(
        @RequestParam("league") Integer league,
        @RequestParam("season") Integer season) {

        try {
                groupService.retrieveAndSaveGroupStageMatches(league, season).block();
                return ResponseEntity.noContent().build();
        } catch (Exception e) {
                return ResponseEntity.status(500).build();
        }
    }

    
    
    
    @Operation(
            summary = "Link matches to groups by home team",
            description = "Associates persisted matches to their groups based on home team"
    )
    @ApiResponse(responseCode = "200", description = "Matches linked successfully")
    @GetMapping("/matches/link")
    public ResponseEntity<Void> linkMatchesToGroups() {
        try {
                groupService.linkMatchesToGroups();
                return ResponseEntity.ok().build();
        } catch (Exception e) {
                return ResponseEntity.status(500).build();
        }
    }

    @Operation(
            summary = "Find all groups with details",
            description = "Retrieves all groups with their matches and metadata"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Groups retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GroupDTO.class)
            )
    )
    @GetMapping("/details")
    public ResponseEntity<List<GroupDTO>> findAllGroupsWithDetails() {
        try {
                List<GroupDTO> groups = groupService.findAllGroupsWithDetails();
                return ResponseEntity.ok(groups);
        } catch (Exception e) {
                return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Get groups by polla",
            description = "Returns the groups (with teams & matches) for the given polla ID")
    @ApiResponse(responseCode = "200",
            description   = "Groups retrieved",
            content       = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-polla/{pollaId}")
    public ResponseEntity<List<GroupDTO>> getGroupsByPolla(@PathVariable Long pollaId) {
        try {
            List<GroupDTO> list = groupService.findGroupsByPolla(pollaId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Esta no la he podido probar bien, es para cuando un torneo esté en vivo y
    // se actualice la información de los equipos dentro de los grupos como lo es rank y poins
    // dado el desempeño de los matches
    @Operation(
            summary = "Update Group Stage",
            description = "Updates group stage by adding new matches and refreshing teams' rank and points"
    )
    @ApiResponse(responseCode = "204", description = "Group stage updated successfully")
    @PutMapping("/update-group")
    public ResponseEntity<Void> updateGroupStage(
        @RequestParam("league") Integer league,
        @RequestParam("season") Integer season) {
        try {
            groupService.updateGroupStage(league, season).block();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
