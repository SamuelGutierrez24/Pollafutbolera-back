package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.exception.CompanyNotFoundException;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.exception.NitExistsException;
import co.edu.icesi.pollafutbolera.exception.NoCompaniesFoundException;
import co.edu.icesi.pollafutbolera.exception.UsernameNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.CompanyMapper;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.CompanyRepository;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.CompanyServiceImpl;
import co.edu.icesi.pollafutbolera.util.CompanyUtil;
import co.edu.icesi.pollafutbolera.util.RoleUtil;
import co.edu.icesi.pollafutbolera.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;


    private CompanyDTO companyDTO;
    private CreateCompanyDTO createCompanyDTO;
    private Company company;
    private User user;

    @BeforeEach
    void setup() {
        companyDTO = CompanyUtil.companyDTO();
        company = CompanyUtil.company();
        user = UserUtil.userDefaultCompany();
        createCompanyDTO = new CreateCompanyDTO(companyDTO, user.getId());
    }

    @Test
    void createCompany_shouldThrowWhenNitAlreadyExists() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(true);

        assertThrows(NitExistsException.class, () -> companyService.createCompany(createCompanyDTO));
    }

    @Test
    void createCompany_shouldReturnBadRequestWhenMappingFails() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenThrow(new RuntimeException());

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createCompany_shouldThrowWhenUserNotFound() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> companyService.createCompany(createCompanyDTO));
    }


    @Test
    void createCompany_shouldReturnBadRequestWhenUserHasCompany() {
        user.setCompany(new Company(666L, "Other Company", "0101010101010", "address", "32565332434","https://placehold.co/400", null, null, null));

        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createCompany_shouldCreateRoleIfNotExists() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(roleRepository.findByName("COMPANY_ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenReturn(RoleUtil.companyAdminRole());

        when(companyRepository.save(company)).thenReturn(company);
        when(userRepository.save(user)).thenReturn(user);
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createCompany_shouldReturnBadRequestWhenCompanySaveFails() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findByName("COMPANY_ADMIN")).thenReturn(Optional.of(RoleUtil.companyAdminRole()));
        doThrow(new RuntimeException()).when(companyRepository).save(company);

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createCompany_shouldReturnBadRequestWhenUserSaveFails() {
        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findByName("COMPANY_ADMIN")).thenReturn(Optional.of(RoleUtil.companyAdminRole()));
        when(companyRepository.save(company)).thenReturn(company);
        doThrow(new RuntimeException()).when(userRepository).save(any());

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createCompany_shouldReturnCreatedSuccessfully() {
        Role role = RoleUtil.companyAdminRole();

        when(companyRepository.existsByNit(CompanyUtil.companyDTO().nit())).thenReturn(false);
        when(companyMapper.toEntity(companyDTO)).thenReturn(company);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findByName("COMPANY_ADMIN")).thenReturn(Optional.of(role));
        when(companyRepository.save(company)).thenReturn(company);
        when(userRepository.save(user)).thenReturn(user);
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        ResponseEntity<CompanyDTO> response = companyService.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(companyDTO, response.getBody());
    }

    @Test
    void findCompanyById_shouldReturnCompanyDTO_whenCompanyExists() {
        Long companyId = 1L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        ResponseEntity<CompanyDTO> response = companyService.findCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDTO, response.getBody());
    }

    @Test
    void findCompanyById_shouldReturnNotFound_whenCompanyDoesNotExist() {
        Long companyId = 1L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        ResponseEntity<CompanyDTO> response = companyService.findCompanyById(companyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void findCompanyByName_shouldReturnCompanyDTO_whenCompanyExists() {
        String name = "ICESI";
        when(companyRepository.findByName(name)).thenReturn(Optional.of(company));
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        ResponseEntity<CompanyDTO> response = companyService.findCompanyByName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDTO, response.getBody());
    }

    @Test
    void findCompanyByName_shouldReturnNotFound_whenCompanyDoesNotExist() {
        String name = "Nonexistent Co.";
        when(companyRepository.findByName(name)).thenReturn(Optional.empty());

        ResponseEntity<CompanyDTO> response = companyService.findCompanyByName(name);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateCompanyById_shouldBeSuccessful() {
        Long companyId = 2L;
        UpdateCompanyDTO updateCompanyDTO = new UpdateCompanyDTO(
                "Nueva Empresa",
                "Calle 25 N",
                null
        );

        Company existingCompany = new Company(companyId, "Empresa Antiguo", "123456", "Antigua Calle", "antigua@gmail.com", null, null);

        CompanyDTO expectedCompanyDTO = new CompanyDTO(companyId, "Nueva Empresa", "123456", "Calle 25 N", "antigua@gmail.com", null);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany)).thenReturn(existingCompany);
        when(companyMapper.toDTO(existingCompany)).thenReturn(expectedCompanyDTO);

        ResponseEntity<CompanyDTO> response = companyService.updateCompany(companyId, updateCompanyDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCompanyDTO, response.getBody());
    }

    @Test
    void deleteCompanyById_shouldBeSuccessful() {
        Long companyId = company.getId();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).delete(company);
        ResponseEntity<CompanyDTO> response = companyService.deleteCompany(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void deleteCompanyById_shouldThrowException(){
        Long companyId = 53L;
        String expectedMessage = "Company with ID " + companyId + " not found";

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class, () -> {
            companyService.deleteCompany(companyId); // Llamada al servicio
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getAllCompanies_ShouldReturnAllCompanies() {

        List<Company> companies = CompanyUtil.createCompanyList();
        List<CompanyDTO> companyDTOs = CompanyUtil.createCompanyDTOList();

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyMapper.toDTOList(companies)).thenReturn(companyDTOs);

        List<CompanyDTO> result = companyService.getAll();


        assertNotNull(result);
        assertEquals(companyDTOs.size(), result.size());
        assertEquals(companyDTOs, result);
        verify(companyRepository).findAll();
        verify(companyMapper).toDTOList(companies);
    }

    @Test
    void getAllCompanies_ShouldThrowNoCompaniesFoundException() {
        // Arrange
        when(companyRepository.findAll()).thenThrow(new NoCompaniesFoundException("No hay empresas registradas"));

        // Act & Assert
        NoCompaniesFoundException exception = assertThrows(NoCompaniesFoundException.class, () -> companyService.getAll());
        assertEquals("No hay empresas registradas", exception.getMessage());

        verify(companyRepository).findAll();
        verifyNoInteractions(companyMapper);
    }

}
