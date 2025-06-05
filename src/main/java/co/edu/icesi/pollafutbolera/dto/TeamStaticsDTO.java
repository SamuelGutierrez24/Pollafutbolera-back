package co.edu.icesi.pollafutbolera.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record TeamStaticsDTO (
        Long id,
        @NotNull
        String name,
        @NotNull
        String logo
)implements Serializable {}

