package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.CompanyController;
import co.edu.icesi.pollafutbolera.controller.UserController;
import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.exception.CompanyNotFoundException;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.service.CompanyService;
import co.edu.icesi.pollafutbolera.service.UserService;
import co.edu.icesi.pollafutbolera.util.CompanyUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void createCompany_shouldReturnCreated() {
        CreateCompanyDTO createCompanyDTO = new CreateCompanyDTO(CompanyUtil.companyDTO(), 1L);
        CompanyDTO expectedCompanyDTO = CompanyUtil.companyDTO();

        when(companyService.createCompany(createCompanyDTO))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(expectedCompanyDTO));

        ResponseEntity<CompanyDTO> response = companyController.createCompany(createCompanyDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCompanyDTO, response.getBody());
        verify(companyService).createCompany(createCompanyDTO);
    }

    @Test
    void createCompany_shouldReturnBadRequest() {
        CreateCompanyDTO invalidCompanyDTO = new CreateCompanyDTO(null, 1L);

        when(companyService.createCompany(invalidCompanyDTO))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<CompanyDTO> response = companyController.createCompany(invalidCompanyDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(companyService).createCompany(invalidCompanyDTO);
    }

    @Test
    void createCompany_shouldReturnConflict() {
        CreateCompanyDTO duplicateNitDTO = new CreateCompanyDTO(CompanyUtil.companyDTO(), 1L);

        when(companyService.createCompany(duplicateNitDTO))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).build());

        ResponseEntity<CompanyDTO> response = companyController.createCompany(duplicateNitDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(companyService).createCompany(duplicateNitDTO);
    }

    @Test
    void getCompanyById_shouldReturnCompany() {
        Long companyId = 1L;
        CompanyDTO expectedCompany = CompanyUtil.companyDTO();

        when(companyService.findCompanyById(companyId))
                .thenReturn(ResponseEntity.ok(expectedCompany));

        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCompany, response.getBody());
        verify(companyService).findCompanyById(companyId);
    }

    @Test
    void getCompanyById_shouldReturnNotFound() {
        Long companyId = 999L;

        when(companyService.findCompanyById(companyId))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(companyService).findCompanyById(companyId);
    }

    @Test
    void getCompanyByName_shouldReturnCompany() {
        String companyName = "Test Company";
        CompanyDTO expectedCompany = CompanyUtil.companyDTO();

        when(companyService.findCompanyByName(companyName))
                .thenReturn(ResponseEntity.ok(expectedCompany));

        ResponseEntity<CompanyDTO> response = companyController.getCompanyByName(companyName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCompany, response.getBody());
        verify(companyService).findCompanyByName(companyName);
    }

    @Test
    void getCompanyByName_shouldReturnNotFound() {
        String companyName = "Unknown Company";

        when(companyService.findCompanyByName(companyName))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<CompanyDTO> response = companyController.getCompanyByName(companyName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(companyService).findCompanyByName(companyName);
    }

    @Test
    public void testUpdateCompanyById_ShouldBeUpdated(){
        // Arrange
        Long companyId = 12L;
        UpdateCompanyDTO updateCompanyDTO = CompanyUtil.createUpdateCompanyDTO("Nueva Empresa", "Calle 25 N", "emp@gmail.com");

        CompanyDTO companyDTO = new CompanyDTO(companyId, "Nueva Empresa","123456", "Calle 25 N", "emp@gmail.com", null);

        when(companyService.updateCompany(companyId, updateCompanyDTO)).thenReturn(new ResponseEntity<>(companyDTO, HttpStatus.OK));

        // Act
        ResponseEntity<CompanyDTO> response = companyController.updateCompany(companyId, updateCompanyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyId, response.getBody().id());
        assertEquals("Nueva Empresa", response.getBody().name());
        assertEquals("Calle 25 N", response.getBody().address());
        assertEquals("emp@gmail.com", response.getBody().contact());
    }

    @Test
    public void testUpdateCompanyById_ShouldReturn404Status(){
        Long companyId = 51L;

        UpdateCompanyDTO updateCompanyDTO = CompanyUtil.createUpdateCompanyDTO("Nueva Empresa", "Calle 25 N", "emp@gmail.com");


        when(companyService.updateCompany(companyId,updateCompanyDTO )).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<CompanyDTO> response = companyController.updateCompany(companyId, updateCompanyDTO);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(companyService).updateCompany(companyId, updateCompanyDTO);
    }

    @Test
    void deleteCompanyById_shoulReturnDeleted(){
        CompanyDTO companyDTO = CompanyUtil.companyDTO();
        Long companyId = CompanyUtil.companyDTO().id();

        when(companyService.deleteCompany(companyId)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(companyDTO));

        ResponseEntity<CompanyDTO> response = companyController.deleteCompany(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDTO, response.getBody());
        verify(companyService).deleteCompany(companyId);
    }

    @Test
    void deleteCompanyById_shouldReturn404Status(){
        Long companyId = 53L;

        when(companyService.deleteCompany(companyId)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<CompanyDTO> response = companyController.deleteCompany(companyId);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllCompanies_ShouldReturnListOfCompanies() {
        // Arrange
        when(companyService.getAll()).thenReturn(CompanyUtil.createCompanyDTOList());

        // Act
        ResponseEntity<List<CompanyDTO>> response = companyController.getAllCompanies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }


}


