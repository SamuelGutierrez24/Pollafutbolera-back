package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.PermissionAPI;
import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.service.PermissionService;
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
@Tag(name = "Permission", description = "This API allows you to manage permissions")
public class PermissionController implements PermissionAPI {

    private final PermissionService permissionService;

    @Override
    @Operation(summary = "Get Permission by ID",
            description = "Retrieve a permission by its ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Permission found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Permission not found")
            })
    public ResponseEntity<PermissionDTO> getPermissionById(Long id) {
        return permissionService.getPermissionById(id);
    }

    @Override
    @Operation(summary = "Get All Permissions",
            description = "Retrieve all permissions",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Permissions retrieved",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionDTO.class)))
            })
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @Override
    @Operation(summary = "Get Permission by Name",
            description = "Retrieve a permission by its name",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Permission found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Permission not found")
            })
    public ResponseEntity<PermissionDTO> getPermissionByName(String name) {
        return permissionService.getPermissionByName(name);
    }

    @Override
    @Operation(summary = "Create Permission",
            description = "Create a new permission",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Permission created",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<PermissionDTO> createPermission(PermissionDTO permissionDTO) {
        return permissionService.createPermission(permissionDTO);
    }

    @Override
    @Operation(summary = "Update Permission",
            description = "Update an existing permission",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Permission updated",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PermissionDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Permission not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<PermissionDTO> updatePermission(Long id, PermissionDTO permissionDTO) {
        return permissionService.updatePermission(id, permissionDTO);
    }

    @Override
    @Operation(summary = "Delete Permission",
            description = "Delete a permission by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Permission deleted"),
                    @ApiResponse(responseCode = "404", description = "Permission not found")
            })
    public ResponseEntity<Void> deletePermission(Long id) {
        return permissionService.deletePermission(id);
    }
}