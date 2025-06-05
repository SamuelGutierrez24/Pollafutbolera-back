package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.MatchApi;
import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.exception.MatchNotFoundException;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.service.MatchService;
import co.edu.icesi.pollafutbolera.service.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Match", description = "This API allows you to manage matches")
public class MatchRestController implements MatchApi {


    private final MatchService matchService;
    private final StageService stageService;



    @Override
    @Operation(summary = "Retrieve all matches",
            description = "Retrieves a list of all matches",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matches retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<MatchDTO>> findAll() {
        try {
            ResponseEntity<List<MatchDTO>> matches = matchService.findAll();
            return matches;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Operation(summary = "Create a new match",
            description = "Creates a new match",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Match created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO matchDTO) {
        try {
            return matchService.createMatch(matchDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @Override
    @Operation(summary = "Retrieve a match by ID",
            description = "Retrieves a match by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match found"),
                    @ApiResponse(responseCode = "404", description = "Match not found")
            })
    public ResponseEntity<MatchDTO> findById(Long id) {
        try {
            return matchService.findById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MatchNotFoundException();
        }
    }

    @Override
    @Operation(summary = "Retrieve matches by stage and tournament",
            description = "Retrieves matches by stage and tournament",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matches found"),
                    @ApiResponse(responseCode = "404", description = "Matches not found")
            })
    public ResponseEntity<List<MatchDTO>> findByStage(Long stage, Long tournament) {
        try {
            return matchService.findByStageAndTournament(stage, tournament);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Operation(summary = "Retrieve matches by tournament",
            description = "Retrieves matches by tournament",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matches found"),
                    @ApiResponse(responseCode = "404", description = "Matches not found")
            })
    public ResponseEntity<List<MatchDTO>> findByTornament(Long tournament) {
        try {
            return matchService.findByTournament(tournament);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Operation(summary = "Update a match",
            description = "Updates an existing match",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Match not found")
            })
    public ResponseEntity<MatchDTO> update(Long id, @RequestBody MatchDTO matchDTO) {
        try {
            return matchService.save(id,matchDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Operation(summary = "Delete a match",
            description = "Deletes a match by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Match deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Match not found")
            })
    public ResponseEntity<Void> delete(Long id) {
        try {
            matchService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<String> handleMatchNotFoundException(MatchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Match not found");
    }


}