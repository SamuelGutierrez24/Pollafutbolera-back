package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.SubPollaAPI;
import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.service.SubPollaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "SubPolla API", description = "API for managing SubPollas")
public class SubPollaController implements SubPollaAPI {

    @Autowired
    SubPollaService subPollaService;

    @Override
    @Operation(summary = "Find SubPollas by Polla ID", description = "Retrieve all SubPollas associated with a specific Polla ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of SubPollas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubPollaGetDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "No SubPollas found for the given Polla ID"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<SubPollaGetDTO>> findByPollaId(Long id) {
        return subPollaService.findByPollaId(id);
    }

    @Override
    @Operation(summary = "Find SubPolla by ID", description = "Retrieve a SubPolla by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of SubPolla",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubPollaGetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "SubPolla not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<SubPollaGetDTO> findById(Long id) {
        return subPollaService.findById(id);
    }

    @Override
    @Operation(summary = "Save a new SubPolla", description = "Save a new SubPolla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful saving of SubPolla",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubPollaGetDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<SubPollaGetDTO> save(SubPollaCreateDTO subPollaCreateDTO) {
        return subPollaService.save(subPollaCreateDTO);
    }

    @Override
    @Operation(summary = "Delete a SubPolla by ID", description = "Delete a SubPolla by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of SubPolla"),
            @ApiResponse(responseCode = "404", description = "SubPolla not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<Void> deleteSubPolla(Long id) {
        return subPollaService.deleteSubPolla(id);
    }

    @Override
    @Operation(summary = "Find all SubPollas", description = "Retrieve all SubPollas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of SubPollas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubPollaGetDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No SubPollas found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<SubPollaGetDTO>> findAll() {
        return subPollaService.findAll();
    }

    @Override
    @Operation(summary = "Find SubPollas by Creator User ID", description = "Retrieve all SubPollas associated with a specific creator user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of SubPollas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubPollaGetDTO[].class))),
                    @ApiResponse(responseCode = "204", description = "No SubPollas found for the given creator user ID"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<SubPollaGetDTO>> findByCreatorUserId(Long creatorUserId) {
        return subPollaService.findByCreatorUserId(creatorUserId);
    }
}