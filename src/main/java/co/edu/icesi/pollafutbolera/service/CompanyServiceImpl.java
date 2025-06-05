package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.exception.CompanyNotFoundException;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.exception.*;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.mapper.CompanyMapper;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.CompanyRepository;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<CompanyDTO> createCompany(CreateCompanyDTO createCompanyDTO) {

        CompanyDTO companyDTO = createCompanyDTO.companyDTO();
        Long companyAdminId = createCompanyDTO.companyAdminId();

        Company company;
        User companyAdmin;

        if(companyRepository.existsByNit(companyDTO.nit()))
            throw new NitExistsException();


        try {
            company = companyMapper.toEntity(companyDTO);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        //set role to company admin

        Optional<User> opCompanyAdmin = userRepository.findById(companyAdminId);

        if(opCompanyAdmin.isPresent()){
            companyAdmin = opCompanyAdmin.get();
        }else{
            throw new UsernameNotFoundException();
        }

        // Validates that the user is not associated with a company, meaning they have the default company
        if(!companyAdmin.getCompany().getName().equals("Default Company")
                || companyAdmin.getCompany() == null){
            return ResponseEntity
                    .badRequest()
                    .build();
        }


        Optional<Role> opRole = roleRepository.findByName("COMPANY_ADMIN");
        Role companyAdminRole;

        if (opRole.isPresent()) {
            companyAdminRole = opRole.get();
        }else {
            companyAdminRole = roleRepository.save(new Role(null, "COMPANY_ADMIN", null, null));
        }

        companyAdmin.setRole(companyAdminRole);

        try {
            companyRepository.save(company);
        }catch (Exception e){
            System.err.println("Error saving company: " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .build();
        }


        try {
            companyAdmin.setCompany(company);
            userRepository.save(companyAdmin);
        }catch (Exception e){
            System.err.println("Error saving company admin: " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        CompanyDTO savedCompanyDTO = companyMapper.toDTO(company);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCompanyDTO);

    }

    @Override
    public ResponseEntity<CompanyDTO> findCompanyById(Long id) {

        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            CompanyDTO companyDTO = companyMapper.toDTO(company.get());
            return ResponseEntity.ok(companyDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<CompanyDTO> findCompanyByName(String companyName) {
        Optional<Company> company = companyRepository.findByName(companyName);
        if (company.isPresent()) {
            CompanyDTO companyDTO = companyMapper.toDTO(company.get());
            return ResponseEntity.ok(companyDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<CompanyDTO> updateCompany(Long id, UpdateCompanyDTO updateCompanyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(()->new CompanyNotFoundException("Company with ID " + id + " not found"));

        companyMapper.updatePartial(updateCompanyDTO, company);

        Company updatedCompany = companyRepository.save(company);

        return ResponseEntity.ok(companyMapper.toDTO(updatedCompany));
    }

    @Override
    public List<CompanyDTO> getAll() {
        List<Company> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            throw new NoCompaniesFoundException("No hay empresas registradas");
        }

        return companyMapper.toDTOList(companies);

    }
    
    @Override
    public ResponseEntity<CompanyDTO> deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with ID " + id + " not found"));
        companyRepository.delete(company);
        CompanyDTO companyDTO = companyMapper.toDTO(company);
        return ResponseEntity.ok(companyDTO);
    }
}
