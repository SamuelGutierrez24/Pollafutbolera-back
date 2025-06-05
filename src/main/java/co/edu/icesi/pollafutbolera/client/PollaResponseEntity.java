package co.edu.icesi.pollafutbolera.client;
import org.springframework.http.HttpStatus;

public record PollaResponseEntity(
        HttpStatus status,
        Object body
) {
}
