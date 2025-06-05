package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.RoleAPI;
import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.service.RoleService;
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
@Tag(name = "Role", description = "This API allows you to manage roles")
public class RoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    @Operation(summary = "Get Role by ID",
            description = "Retrieve a role by its ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Role found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Role not found")
            })
    public ResponseEntity<RoleDTO> getRoleById(Long id) {
        return roleService.getRoleById(id);
    }

    @Override
    @Operation(summary = "Get All Roles",
            description = "Retrieve all roles",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Roles retrieved",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDTO.class)))
            })
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    @Operation(summary = "Get Role by Name",
            description = "Retrieve a role by its name",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Role found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Role not found")
            })
    public ResponseEntity<RoleDTO> getRoleByName(String name) {
        return roleService.getRoleByName(name);
    }

    @Override
    @Operation(summary = "Create Role",
            description = "Create a new role",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Role created",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<RoleDTO> createRole(RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }

    @Override
    @Operation(summary = "Update Role",
            description = "Update an existing role",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Role updated",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoleDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<RoleDTO> updateRole(Long id, RoleDTO roleDTO) {
        return roleService.updateRole(id, roleDTO);
    }

    @Override
    @Operation(summary = "Delete Role",
            description = "Delete a role by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role deleted"),
                    @ApiResponse(responseCode = "404", description = "Role not found")
            })
    public ResponseEntity<Void> deleteRole(Long id) {
        return roleService.deleteRole(id);
    }
}