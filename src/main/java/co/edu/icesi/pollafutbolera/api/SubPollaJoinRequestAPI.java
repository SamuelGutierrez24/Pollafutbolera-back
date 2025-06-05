package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestResponseDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subpolla Join Request API", description = "API to handle join requests to SubPollas")
@RequestMapping("/join-request")
public interface SubPollaJoinRequestAPI {

    @Operation(summary = "Create a request to join a subpolla")
    @PostMapping("/request")
    @PreAuthorize("permitAll()")
    ResponseEntity<SubPollaJoinRequestGetDTO> createJoinRequest(@RequestBody SubPollaJoinRequestCreateDTO dto);

    @Operation(summary = "Get all requests")
    @GetMapping
    @PreAuthorize("permitAll()")
    ResponseEntity<List<SubPollaJoinRequestGetDTO>> getAllRequests();

    @Operation(summary = "Respond to a join request")
    @PatchMapping("/{requestId}/response")
    @PreAuthorize("permitAll()")
    ResponseEntity<SubPollaJoinRequestGetDTO> respondToJoinRequest(
            @PathVariable Long requestId,
            @RequestBody SubPollaJoinRequestResponseDTO responseDTO);

    @Operation(summary = "Get all join requests for a specific subpolla")
    @GetMapping("/subpolla/{subpollaId}")
    @PreAuthorize("permitAll()")
    ResponseEntity<List<SubPollaJoinRequestGetDTO>> getRequestsBySubpolla(@PathVariable Long subpollaId);


}
