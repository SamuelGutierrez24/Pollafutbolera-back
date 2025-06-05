package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.model.Role;
import lombok.Builder;

@Builder
public record LoginOutDTO(
        Long userId,
        String userUsername,
        String userExtId,
        String userEmail,
        String userPhone,
        String nickname,
        String userLastname,
        String userDocumentId,
        String accessToken,
        String tokenType,
        String systemHomePage,
        Long company,
        Role role
) {
}
