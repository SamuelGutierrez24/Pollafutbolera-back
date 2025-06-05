package co.edu.icesi.pollafutbolera.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record NotificationSettingsDTO(
    Long userId,
    Boolean enabledEmail,
    Boolean enabledSMS,
    Boolean enabledWhatsapp
) implements Serializable { } 