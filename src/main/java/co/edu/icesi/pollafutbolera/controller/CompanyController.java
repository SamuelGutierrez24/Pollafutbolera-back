package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.CompanyAPI;
import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Company API", description = "API for managing companies")
public class CompanyController implements CompanyAPI {

    private final CompanyService companyService;

    @Override
    @Operation(summary = "Create company",
            description = "Create a new company",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Company created",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "Nit already exists")
            })
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CreateCompanyDTO createCompanyDTO) {
        return companyService.createCompany(createCompanyDTO);
    }
    @Override
    @Operation(summary = "Get Company by ID",
            description = "Retrieve a company by their ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Company found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            })
    public ResponseEntity<CompanyDTO> getCompanyById(Long companyId) {
        return companyService.findCompanyById(companyId);
    }

    @Override
    @Operation(summary = "Get Company by anme",
            description = "Retrieve a company by their name",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Company found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            })
    public ResponseEntity<CompanyDTO> getCompanyByName(String companyName) {
        return companyService.findCompanyByName(companyName);
    }

    @Override
    @Operation(summary = "Update Company by ID",
            description = "Update the details of a company by providing its ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Company updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            })
    public ResponseEntity<CompanyDTO> updateCompany(Long id, UpdateCompanyDTO updateCompanyDTO) {
        return companyService.updateCompany(id, updateCompanyDTO);
    }


    @Override
    @Operation(summary = "Get All Companies",
            description = "Retrieve a list of all companies",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of companies found",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No companies found")
            })
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAll();
        return ResponseEntity.ok(companies);
    }



    @Operation(summary = "Delete a company by ID",
            description = "Deletes a company by its ID from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company deleted successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content
                                    (mediaType = "application/json",
                                            schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            })
    public ResponseEntity<CompanyDTO> deleteCompany(Long id) {
        return companyService.deleteCompany(id);
    }

}
