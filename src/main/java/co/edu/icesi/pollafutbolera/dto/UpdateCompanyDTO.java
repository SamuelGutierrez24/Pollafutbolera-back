
package co.edu.icesi.pollafutbolera.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.io.Serializable;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateCompanyDTO(
        String name,
        String address,
        String contact
) implements Serializable {}