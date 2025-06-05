package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

@Builder
public record ChangePasswordRequest(String token, String newPassword
) {

}
