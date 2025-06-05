package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.SubPollaJoinRequestAPI;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestResponseDTO;
import co.edu.icesi.pollafutbolera.service.SubPollaJoinRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubPollaJoinRequestController implements SubPollaJoinRequestAPI {

    private final SubPollaJoinRequestService service;

    @Override
    @Operation(summary = "Send join request", description = "Allows a user to send a join request to a SubPolla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request sent successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubPollaJoinRequestGetDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    public ResponseEntity<SubPollaJoinRequestGetDTO> createJoinRequest(SubPollaJoinRequestCreateDTO dto) {
        return service.createJoinRequest(dto);
    }

    @Override
    @Operation(summary = "Get all join requests", description = "Retrieve all join requests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Join requests retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubPollaJoinRequestGetDTO[].class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    public ResponseEntity<List<SubPollaJoinRequestGetDTO>> getAllRequests() {
        return service.getAllRequests();
    }

    @Override
    @Operation(summary = "Respond to a join request", description = "Allows the SubPolla admin to accept or reject a join request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request responded successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubPollaJoinRequestGetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Join request not found"),
                    @ApiResponse(responseCode = "403", description = "Only the SubPolla creator can respond to requests"),
                    @ApiResponse(responseCode = "400", description = "Invalid response format")
            })
    public ResponseEntity<SubPollaJoinRequestGetDTO> respondToJoinRequest(Long requestId, SubPollaJoinRequestResponseDTO responseDTO) {
        return service.respondToJoinRequest(requestId, responseDTO);
    }

    @Override
    @Operation(summary = "Get all join requests by SubPolla", description = "Retrieve all join requests associated with a specific SubPolla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Join requests retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubPollaJoinRequestGetDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "SubPolla not found")
            })
    public ResponseEntity<List<SubPollaJoinRequestGetDTO>> getRequestsBySubpolla(Long subpollaId) {
        return service.getRequestsBySubpolla(subpollaId);
    }


}
