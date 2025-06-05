package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

@Builder
public record PreloadUserErrorDTO(
        Integer lineNumber,
        String cedula,
        String name,
        String lastName,
        String mail,
        String errorMessage
) {}
