package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record RoleDTO(
        Long id,
        @NotNull
        @Size(max = 100)
        String name,
        List<PermissionDTO> permissions
) implements Serializable {
}