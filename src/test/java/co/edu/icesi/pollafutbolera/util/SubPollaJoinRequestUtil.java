package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestResponseDTO;
import co.edu.icesi.pollafutbolera.enums.SubPollaJoinRequestStatus;
import co.edu.icesi.pollafutbolera.model.*;

public class SubPollaJoinRequestUtil {

    public static User user() {
        User user = new User();
        user.setId(1L);
        user.setNickname("testuser");
        return user;
    }

    public static User admin() {
        User admin = new User();
        admin.setId(2L);
        admin.setNickname("adminuser");
        return admin;
    }

    public static SubPolla subpolla(User creator) {
        SubPolla subPolla = new SubPolla();
        subPolla.setId(10L);
        subPolla.setUser(creator);
        subPolla.setIsPrivate(false);
        subPolla.setDeletedAt(null);
        return subPolla;
    }

    public static SubPollaJoinRequest joinRequestPending(User user, SubPolla subPolla) {
        return SubPollaJoinRequest.builder()
                .id(100L)
                .user(user)
                .subpolla(subPolla)
                .status(SubPollaJoinRequestStatus.PENDING)
                .build();
    }

    public static SubPollaJoinRequest joinRequestAccepted(User user, SubPolla subPolla) {
        return SubPollaJoinRequest.builder()
                .id(101L)
                .user(user)
                .subpolla(subPolla)
                .status(SubPollaJoinRequestStatus.ACCEPTED)
                .build();
    }

    public static SubPollaJoinRequestCreateDTO createDTO() {
        return new SubPollaJoinRequestCreateDTO(user().getId(), subpolla(admin()).getId());
    }

    public static SubPollaJoinRequestResponseDTO acceptResponse() {
        return new SubPollaJoinRequestResponseDTO("ACCEPT");
    }

    public static SubPollaJoinRequestResponseDTO rejectResponse() {
        return new SubPollaJoinRequestResponseDTO("REJECT");
    }

    public static SubPollaJoinRequestGetDTO joinRequestGetDTO() {
        return new SubPollaJoinRequestGetDTO(
                100L,
                user().getId(),
                subpolla(admin()).getId(),
                SubPollaJoinRequestStatus.PENDING
        );
    }

    public static UserSubPolla userSubPolla(User user, SubPolla subPolla) {
        return UserSubPolla.builder()
                .user(user)
                .subpolla(subPolla)
                .build();
    }

    public static TypeNotification typeNotification() {
        TypeNotification tn = new TypeNotification();
        tn.setId(1L);
        tn.setName("JOIN_REQUEST");
        return tn;
    }

}
