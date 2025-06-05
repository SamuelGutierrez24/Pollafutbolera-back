package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.UserAPI;

import co.edu.icesi.pollafutbolera.dto.*;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;

import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.UserService;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "User", description = "This API allows you all to manage users")
public class UserController implements UserAPI {

    private final UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Override
    @Operation(summary = "Login",
            description = "Login to the system",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Login successful",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginOutDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<LoginOutDTO> login(LoginInDTO loginInDTO) {
        return ResponseEntity.ok(userService.login(loginInDTO));
    }


    @Override
    @Operation(summary = "Change Password",
            description = "Allows users to reset their password using a token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid token or user not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {

        boolean isChanged = userService.changePassword(request.token(), request.newPassword());
        if (isChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or user not found");
        }
    }

    @Operation(summary = "Get Users by Polla ID",
            description = "Retrieves a list of users associated with a specific Polla",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "204", description = "No users found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @Override
    public ResponseEntity<List<UserDTO>> getUsersByPollaId(@PathVariable Long pollaId) {
        return userService.getUsersByPollaId(pollaId);
    }

    @Operation(summary = "Ban User from Polla",
            description = "Bans a user from a specific Polla",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User banned successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @Override
    public ResponseEntity<Boolean> banUserFromPolla(@PathVariable Long userId, @PathVariable Long pollaId) {
            return userService.banUserFromPolla(userId, pollaId);
    }

    @Operation(summary = "Get Users by Default Company",
            description = "Retrieve all users associated with the default company",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))
                            )),
                    @ApiResponse(responseCode = "204",
                            description = "No users found for the default company")
    })
    public ResponseEntity<List<UserDTO>> getUsersByDefaultCompany() {
        return userService.getUsersByDefaultCompany();
    }

    @Override
    @Operation(summary = "Get User by ID",
            description = "Retrieve a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    @Operation(summary = "Get All Users",
            description = "Retrieve all users",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Users retrieved",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class)))
            })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    @Operation(summary = "Create User",
            description = "Create a new user",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "User created",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public PollaResponseEntity createUser(UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @Override
    @Operation(summary = "Update User",
            description = "Update an existing user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User updated",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public PollaResponseEntity updateUser(Long id, UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Override
    @Operation(summary = "Delete User",
            description = "Delete a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<Void> deleteUser(Long id) {
        return userService.deleteUser(id);
    }

    @Override
    @Operation(summary = "Update notification settings",
            description = "Update a user's notification preferences",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Settings updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationSettingsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(Long id, NotificationSettingsDTO settings) {
        return userService.updateNotificationSettings(id, settings);
    }

    @Override
    @Operation(summary = "Get notification settings",
            description = "Get a user's current notification preferences",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Settings retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationSettingsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<NotificationSettingsDTO> getNotificationSettings(Long id) {
        return userService.getNotificationSettings(id);
    }

    @Override
    @Operation(summary = "Check if nickname exists",
            description = "Check if a nickname already exists in the system",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Nickname existence checked successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<Boolean> nicknameExists(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.nicknameExists(nickname));
    }

    //Extract jwt from request and pass it to the service with the DTO
    @Override
    @Operation(summary = "Update Current User",
            description = "Update the current user's information",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<UpdatedUserDTO> updateCurrentUser(@RequestBody UserDTO userDTO, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build(); // Return 400 if the token is missing or invalid
        }

        // Extract the JWT token
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix

        // Pass the JWT and userDTO to the service
        return userService.updateCurrentUser(jwtToken, userDTO);
    }

    @Override
    @Operation(summary = "Get Current User",
            description = "Retrieve the current user's information",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<UserDTO> getCurrentUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build(); // Return 400 if the token is missing or invalid
        }

        // Extract the JWT token
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix

        // Pass the JWT to the service
        return userService.getCurrentUser(jwtToken);
    }

    @Override
    @Operation(summary = "Preload Users from File",
            description = "Uploads a CSV or Excel file containing user information and preloads the users into the system.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Users preloaded successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PreloadUserErrorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file format or missing data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PreloadUserValidationResultDTO> preloadUsers(
            @PathVariable Long pollaId,
            @RequestBody MultipartFile file,
            @RequestHeader("X-Company-Id") Long companyId) {
        return userService.preloadUsers(pollaId, file, companyId);
    }

    @Override
    @Operation(summary = "Get Preloaded Users by Polla ID",
            description = "Retrieve preloaded users associated with a specific Polla, with pagination.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Preloaded users retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PreloadUserDTO.class))),
                    @ApiResponse(responseCode = "204", description = "No preloaded users found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Page<PreloadUserDTO>> getPreloadedUsersByPollaId(@PathVariable Long pollaId, Pageable pageable) {
        return userService.getPreloadedUsersByPollaId(pollaId, pageable);
    }

    @Override
    @Operation(summary = "Get Preloaded User by Cedula",
            description = "Retrieve preloaded users associated with a specific Polla, with pagination.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Preloaded users retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PreloadUserDTO.class))),
                    @ApiResponse(responseCode = "204", description = "No preloaded user found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PreloadUserDTO> getPreloadedUsersById(@PathVariable String id){
        return userService.getPreloadedUsersById(id);
    }
}
