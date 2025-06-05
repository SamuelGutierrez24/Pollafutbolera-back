package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.FixtureAPI;
import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import co.edu.icesi.pollafutbolera.service.FixtureServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Match stadistics", description = "This API allows you to get statics per match or fixture")
public class FixtureController implements FixtureAPI {


    FixtureServiceImpl fixtureService;
    @Override
    @Operation(summary = "Get Statics by ID of fixture",
            description = "Retrieve JSON with all the important data based on fixture",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Statics found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FixtureDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error server")
            })
    public ResponseEntity<?> getStatidistics(Long id) {
        try{
            return ResponseEntity.ok(fixtureService.fixtureStatics(id));

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
