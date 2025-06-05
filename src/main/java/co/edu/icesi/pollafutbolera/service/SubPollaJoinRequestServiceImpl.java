package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestResponseDTO;
import co.edu.icesi.pollafutbolera.enums.SubPollaJoinRequestStatus;
import co.edu.icesi.pollafutbolera.mapper.SubPollaJoinRequestMapper;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubPollaJoinRequestServiceImpl implements SubPollaJoinRequestService {

    private final SubPollaJoinRequestRepository repository;
    private final UserRepository userRepository;
    private final SubPollaRepository subpollaRepository;
    private final SubPollaJoinRequestMapper mapper;
    private final UserSubPollaRepository userSubPollaRepository;
    private final NotificationServiceImpl notificationService;
    private final TypeNotificationService typeNotificationService;
    private final TypeNotificationRepository typeNotificationRepository;

    @Override
    public ResponseEntity<SubPollaJoinRequestGetDTO> createJoinRequest(SubPollaJoinRequestCreateDTO dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow();
        SubPolla subpolla = subpollaRepository.findById(dto.subpollaId()).orElseThrow();

        SubPollaJoinRequest request = SubPollaJoinRequest.builder()
                .user(user)
                .subpolla(subpolla)
                .status(SubPollaJoinRequestStatus.PENDING)
                .build();

        SubPollaJoinRequest savedRequest = repository.save(request);

        User admin = subpolla.getUser();
        String content = String.format("El usuario %s ha solicitado unirse a tu subpolla.", user.getNickname());

        TypeNotification type = typeNotificationRepository.findByName("JOIN_REQUEST");

        if (type == null) {
            throw new IllegalStateException("Tipo de notificación 'JOIN_REQUEST' no encontrado");
        }

        notificationService.createNotification(admin, content, type);

        return ResponseEntity.ok(mapper.toDTO(savedRequest));
    }

    @Override
    public ResponseEntity<List<SubPollaJoinRequestGetDTO>> getAllRequests() {
        return ResponseEntity.ok(repository.findAll().stream().map(mapper::toDTO).toList());
    }

    @Override
    public ResponseEntity<SubPollaJoinRequestGetDTO> respondToJoinRequest(Long requestId, SubPollaJoinRequestResponseDTO dto) {
        SubPollaJoinRequest request = repository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Join request not found"));

        SubPolla subPolla = request.getSubpolla();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();

        User admin = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        if (!subPolla.getUser().getId().equals(admin.getId())) {
            throw new SecurityException("Only the creator of the SubPolla can respond to requests");
        }

        switch (dto.decision().toUpperCase()) {
            case "ACCEPT":
                request.setStatus(SubPollaJoinRequestStatus.ACCEPTED);

                User userToAdd = request.getUser();

                UserSubPolla userSubPolla = UserSubPolla.builder()
                        .user(userToAdd)
                        .subpolla(subPolla)
                        .build();

                userSubPollaRepository.save(userSubPolla);

                TypeNotification type = typeNotificationRepository.findByName("JOIN_REQUEST");

                if (type == null) {
                    throw new IllegalStateException("Tipo de notificación 'JOIN_REQUEST' no encontrado");
                }

                notificationService.createNotification(
                        userToAdd,
                        "Tu solicitud para unirte a la SubPolla ha sido aceptada.",
                        type
                );
                break;
            case "REJECT":
                request.setStatus(SubPollaJoinRequestStatus.REJECTED);
                break;
            default:
                throw new IllegalArgumentException("Invalid decision. Use 'ACCEPT' or 'REJECT'");
        }

        SubPollaJoinRequest updated = repository.save(request);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @Override
    public ResponseEntity<List<SubPollaJoinRequestGetDTO>> getRequestsBySubpolla(Long subpollaId) {
        List<SubPollaJoinRequest> requests = repository.findBySubpollaId(subpollaId);
        List<SubPollaJoinRequestGetDTO> dtos = requests.stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}
