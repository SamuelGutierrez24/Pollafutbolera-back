package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.RoleController;
import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.service.RoleService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private RoleDTO testRoleDTO;
    private PermissionDTO testPermissionDTO;

    @BeforeEach
    void setUp() {
        testPermissionDTO = PermissionDTO.builder()
                .id(1L)
                .name("READ_PRIVILEGES")
                .build();

        testRoleDTO = RoleDTO.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(List.of(testPermissionDTO))
                .build();
    }

    @Test
    void getRoleById_ShouldReturnRoleDTO_WhenRoleExists() {
        when(roleService.getRoleById(1L)).thenReturn(new ResponseEntity<>(testRoleDTO, HttpStatus.OK));

        ResponseEntity<RoleDTO> response = roleController.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void getRoleById_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleService.getRoleById(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<RoleDTO> response = roleController.getRoleById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoleDTOs() {
        List<RoleDTO> roleDTOs = List.of(testRoleDTO);

        when(roleService.getAllRoles()).thenReturn(new ResponseEntity<>(roleDTOs, HttpStatus.OK));

        ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleDTOs, response.getBody());
    }

    @Test
    void getRoleByName_ShouldReturnRoleDTO_WhenRoleExists() {
        when(roleService.getRoleByName("ADMIN")).thenReturn(new ResponseEntity<>(testRoleDTO, HttpStatus.OK));

        ResponseEntity<RoleDTO> response = roleController.getRoleByName("ADMIN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void getRoleByName_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleService.getRoleByName("ADMIN")).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<RoleDTO> response = roleController.getRoleByName("ADMIN");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createRole_ShouldReturnCreatedRoleDTO() {
        when(roleService.createRole(testRoleDTO)).thenReturn(new ResponseEntity<>(testRoleDTO, HttpStatus.CREATED));

        ResponseEntity<RoleDTO> response = roleController.createRole(testRoleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void updateRole_ShouldReturnUpdatedRoleDTO_WhenRoleExists() {
        when(roleService.updateRole(1L, testRoleDTO)).thenReturn(new ResponseEntity<>(testRoleDTO, HttpStatus.OK));

        ResponseEntity<RoleDTO> response = roleController.updateRole(1L, testRoleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void updateRole_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleService.updateRole(1L, testRoleDTO)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<RoleDTO> response = roleController.updateRole(1L, testRoleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteRole_ShouldReturnNoContent_WhenRoleExists() {
        when(roleService.deleteRole(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = roleController.deleteRole(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteRole_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleService.deleteRole(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<Void> response = roleController.deleteRole(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}