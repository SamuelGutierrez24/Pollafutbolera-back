package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCompanyDTO(
        @NotNull
        CompanyDTO companyDTO,
        @NotNull
        Long companyAdminId
) { }
