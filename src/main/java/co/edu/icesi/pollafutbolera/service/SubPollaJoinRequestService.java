package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubPollaJoinRequestService {

    ResponseEntity<SubPollaJoinRequestGetDTO> createJoinRequest(SubPollaJoinRequestCreateDTO dto);

    ResponseEntity<List<SubPollaJoinRequestGetDTO>> getAllRequests();

    ResponseEntity<SubPollaJoinRequestGetDTO> respondToJoinRequest(Long requestId, SubPollaJoinRequestResponseDTO dto);

    ResponseEntity<List<SubPollaJoinRequestGetDTO>> getRequestsBySubpolla(Long subpollaId);

}
