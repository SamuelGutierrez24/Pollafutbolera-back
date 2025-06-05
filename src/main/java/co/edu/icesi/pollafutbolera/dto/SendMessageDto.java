package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

@Builder
public record SendMessageDto(
        String to,
        String message
) { }
