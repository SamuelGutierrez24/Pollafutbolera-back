package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record PreloadUserDTO(
        Long id,
        @NotNull @Size(max = 20) String cedula,
        @NotNull @Size(max = 255) String companyName,
        @NotNull @Size(max = 255) String name,
        @NotNull @Size(max = 255) String lastName,
        @Size(max = 20) String phoneNumber,
        @NotNull @Size(max = 255) String mail,
        String extraInfo,
        @NotNull Long pollaId
) implements Serializable {
}
