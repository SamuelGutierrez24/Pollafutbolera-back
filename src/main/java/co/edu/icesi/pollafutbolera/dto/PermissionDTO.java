package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.io.Serializable;

@Builder
public record PermissionDTO(
        Long id,
        @NotNull
        @Size(max = 100)
        String name
) implements Serializable {
}