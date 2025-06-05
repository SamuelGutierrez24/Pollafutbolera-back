package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.UserScoresPollaAPI;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.RankingDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.service.UserScoresPollaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "UserScoresPolla API", description = "API para gestionar los puntajes de los usuarios en una polla")
public class UserScoresPollaController implements UserScoresPollaAPI {

    private final UserScoresPollaService userScoresPollaService;

    @Override
    @Operation(summary = "Actualizar puntajes de usuarios por polla",
            description = "Actualiza los puntajes de los usuarios asociados a una polla específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Puntajes actualizados exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "404", description = "Polla o usuarios no encontrados"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public ResponseEntity<Boolean> updateUserScoresByPolla(@PathVariable Long pollaId) {
        return userScoresPollaService.updateUserScoresByPolla(pollaId);
    }

    @Override
    @Operation(summary = "Obtener IDs de pollas por usuario",
            description = "Obtiene una lista de IDs de pollas asociadas a un usuario específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de IDs de pollas obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "204", description = "No se encontraron pollas para el usuario"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public ResponseEntity<List<Long>> getPollaIdsByUserId(@PathVariable Long userId) {
        return userScoresPollaService.getPollaIdsByUserId(userId);
    }

    @Override
    @Operation(summary = "Actualizar puntajes de usuarios por partido",
               description = "Actualiza los puntajes de los usuarios basándose en los resultados de un partido específico",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Puntajes actualizados exitosamente",
                                content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = Boolean.class))),
                   @ApiResponse(responseCode = "404", description = "Partido o usuarios no encontrados"),
                   @ApiResponse(responseCode = "500", description = "Error interno del servidor")
               })
    public ResponseEntity<Boolean> updateUserScoresByMatch(@PathVariable Long matchId) {
        return userScoresPollaService.updateUserScoresByMatch(matchId);
    }

    @Override
    @Operation(summary = "Obtener pollas por usuario",
            description = "Obtiene una lista de las pollas asociadas a un usuario específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pollas obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "204", description = "No se encontraron pollas para el usuario"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public ResponseEntity<List<PollaGetDTO>> getPollaByUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build(); // Return 400 if the token is missing or invalid
        }

        // Extract the JWT token
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix

        return userScoresPollaService.getPollasByUserId(jwtToken);
    }

    @Override
    @Operation(summary = "Crear relación entre usuario y polla",
            description = "Crea una relación entre un usuario y una polla específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relación creada exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario o polla no encontrados"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public ResponseEntity<Boolean> createRelation(@PathVariable Long userId, @PathVariable Long pollaId) {
        return userScoresPollaService.createRelation(userId, pollaId);
    }

    @Operation(
            summary = "Get ranking of users in a polla",
            description = "Retrieves a paginated ranking of users based on their scores in a specific polla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ranking retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters, non existent polla"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Polla not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @Override
    public ResponseEntity<List<RankingDTO>> getRankingPolla(Long pollaId, Pageable pageable) {
        return userScoresPollaService.rankingByPolla(pollaId, pageable);
    }

    @Operation(
            summary = "Get ranking of users in a sub-polla",
            description = "Retrieves a paginated ranking of users based on their scores in a specific sub-polla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ranking retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters or non-existent sub-polla"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sub-polla not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @Override
    public ResponseEntity<List<RankingDTO>> getRankingSubPolla(Long subPollaId, Pageable pageable) {
        return userScoresPollaService.rankingBySubPolla(subPollaId, pageable);
    }

    @Operation(
            summary = "Get user position in polla ranking",
            description = "Retrieves the ranking position of a specific user in a polla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User ranking position retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found in the polla, or polla not found in databas"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @Override
    public ResponseEntity<Long> findRankingByUserIdAndPollaId(Long userId, Long pollaId) {
        return userScoresPollaService.findRankingByUserIdAndPollaId(userId, pollaId);
    }


    @Operation(
            summary = "Get user position in sub-polla ranking",
            description = "Retrieves the ranking position of a specific user in a sub-polla.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User ranking position retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found in the sub-polla, or sub polla not found in the database"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @Override
    public ResponseEntity<Long> findRankingSubPolla(Long userId, Long subPollaId) {
        return userScoresPollaService.findRankingSubPolla(userId, subPollaId);
    }


}
