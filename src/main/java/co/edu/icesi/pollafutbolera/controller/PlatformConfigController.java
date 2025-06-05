package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.PlatformConfigAPI;
import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.service.PlatformConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "PlatformConfig", description = "This API allows you to manage the platform configuration rules")
public class PlatformConfigController implements PlatformConfigAPI {

    private final PlatformConfigService platformConfigService;

    @Override
    @Operation(summary = "Get a platform configuration rule by its identifier",
            description = "Retrieve a platform configuration rule by its identifier",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Platform configuration rule retrieved",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformConfigDTO.class)))
            })
    public ResponseEntity<PlatformConfigDTO> getPlatformConfigById(Long id) {
        return platformConfigService.getPlatformConfigById(id);
    }

    @Override
    @Operation(summary = "Get all platform configuration rules",
            description = "Retrieve all platform configuration rules",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Platform configuration rules retrieved",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformConfigDTO.class)))
            })
    public ResponseEntity<List<PlatformConfigDTO>> getAllPlatformConfigs() {
        return platformConfigService.getAllPlatformConfigs();
    }
}
