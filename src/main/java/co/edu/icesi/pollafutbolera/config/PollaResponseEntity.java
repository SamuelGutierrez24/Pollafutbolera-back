package co.edu.icesi.pollafutbolera.config;

import org.springframework.http.HttpStatus;

public record PollaResponseEntity(
        HttpStatus status,
        Object body
) {
}
