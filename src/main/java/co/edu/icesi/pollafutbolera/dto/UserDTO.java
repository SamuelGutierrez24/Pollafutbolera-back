package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserDTO(
        Long id,
        @NotNull
        @Size(max = 20)
        String cedula,
        Long company,
        @NotNull
        @Size(max = 255)
        String name,
        @Size(max = 255)
        String lastName,
        @NotNull
        String password,
        @NotNull
        @Email
        @Size(max = 255)
        String mail,
        @Size(max = 100)
        String nickname,
        @Size(max = 20)
        String phoneNumber,
        String photo,
        @NotNull
        Long role,
        String extraInfo,
        @NotNull
        Boolean notificationsEmailEnabled,
        @NotNull
        Boolean notificationsSMSEnabled,
        @NotNull
        Boolean notificationsWhatsappEnabled
) implements Serializable {
}
