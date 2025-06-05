package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RewardSaveDTO(
        @NotNull
        @Size(max = 255)
        String name,
        @Size(max = 1000)
        String description,
        String image,
        Integer position,
        Long pollaId
) implements Serializable {
}
