package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Builder
public record CompanyDTO(
        Long id,
        @NotNull
        @Size(max = 100)
        String name,
        @NotNull
        @Size(max = 100)
        String nit,
        @Size(max = 100)
        String address,
        @Size(max = 100)
        String contact,
        @Size(max = 150)
        String logo
) implements Serializable {
}