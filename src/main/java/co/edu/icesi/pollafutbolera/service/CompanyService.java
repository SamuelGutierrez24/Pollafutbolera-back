package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompanyService {

    ResponseEntity<CompanyDTO> createCompany(CreateCompanyDTO createCompanyDTO);

    ResponseEntity<CompanyDTO> findCompanyById(Long id);
    ResponseEntity<CompanyDTO> deleteCompany(Long id);
    ResponseEntity<CompanyDTO> findCompanyByName(String companyName);

    ResponseEntity<CompanyDTO> updateCompany(Long id, UpdateCompanyDTO updateCompanyDTO);

    List<CompanyDTO> getAll();


}