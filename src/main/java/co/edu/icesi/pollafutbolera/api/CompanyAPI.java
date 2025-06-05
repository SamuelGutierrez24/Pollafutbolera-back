package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Company API", description = "API for managing companies")
@RequestMapping("/company")
public interface CompanyAPI {

    @PostMapping("/save")
    ResponseEntity<CompanyDTO> createCompany(@RequestBody CreateCompanyDTO createCompanyDTO);

    @GetMapping("/id/{companyId}")
    ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long companyId);

    @PatchMapping("/{id}")
    ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @Valid @RequestBody UpdateCompanyDTO updateCompanyDTO);

    @GetMapping("/name/{companyName}")
    ResponseEntity<CompanyDTO> getCompanyByName(@PathVariable String companyName);

    @GetMapping("/companies")
    ResponseEntity<List<CompanyDTO>> getAllCompanies();

    @DeleteMapping("/{id}")
    ResponseEntity<CompanyDTO> deleteCompany(@PathVariable Long id);


}
