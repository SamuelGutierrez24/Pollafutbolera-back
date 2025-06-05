package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.PollaAPI;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import co.edu.icesi.pollafutbolera.service.PollaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Polla API", description = "API for managing pollas")
public  class PollaController implements PollaAPI {

    private final PollaService pollaService;

    @Override
    @Operation(summary = "Get all pollas", description = "Retrieve all pollas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of pollas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PollaGetDTO[].class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PollaGetDTO[]> getPolla() {
        return pollaService.findAllPollas();
    }

    @Override
    @Operation(summary = "Delete a polla by ID", description = "Delete a polla by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful deletion of polla"),
                    @ApiResponse(responseCode = "404", description = "Polla not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    public void deletePolla(Long id) {
        pollaService.deletePolla(id);
    }

    @Override
    @Operation(summary = "Get polla by ID", description = "Retrieve a polla by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of polla",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PollaGetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Polla not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PollaGetDTO> getPolla(Long id) {
        return pollaService.findPollaById(id);
    }

    @Override
    @Operation(summary = "Save a new polla", description = "Save a new polla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful saving of polla",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PollaGetDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PollaGetDTO> savePolla(PollaConfigDTO polla) {
        return pollaService.savePolla(polla);
    }


    @Override
    @Operation(summary = "Find pollas by company ID", description = "Retrieve all pollas for a specific company",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of pollas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PollaGetDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Company not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PollaGetDTO[]> findPollasByCompanyId(Long id) {
        return pollaService.findPollasByCompanyId(id);
    }

    @Override
    public ResponseEntity<List<UserScoresPolla>> finishPolla(Long id) {
        return pollaService.endPolla(id);
    }

    @Override
    @Operation(summary = "Update an existing polla", description = "Update an existing polla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful update of polla"),
                    @ApiResponse(responseCode = "404", description = "Polla not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PollaGetDTO> updatePolla(@PathVariable Long id, @RequestBody PollaConfigDTO polla){
        //System.out.println("I'm Here");
        return pollaService.updatePolla(id, polla);
    }
}