package co.edu.icesi.pollafutbolera.dto;

import co.edu.icesi.pollafutbolera.enums.UserPollaState;
import co.edu.icesi.pollafutbolera.model.User;
import jakarta.persistence.*;
import lombok.Builder;

@Builder
public record UserScoresPollaDTO(
        Long id,
        Long userId,
        Long pollaId,
        UserPollaState state,
        int scores
) {
}
