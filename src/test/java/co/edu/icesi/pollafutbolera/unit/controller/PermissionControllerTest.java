package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.PermissionController;
import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.service.PermissionService;
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
class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    private PermissionDTO testPermissionDTO;

    @BeforeEach
    void setUp() {
        testPermissionDTO = PermissionDTO.builder()
                .id(1L)
                .name("READ_PRIVILEGES")
                .build();
    }

    @Test
    void getPermissionById_ShouldReturnPermissionDTO_WhenPermissionExists() {
        when(permissionService.getPermissionById(1L)).thenReturn(new ResponseEntity<>(testPermissionDTO, HttpStatus.OK));

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void getPermissionById_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionService.getPermissionById(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllPermissions_ShouldReturnListOfPermissionDTOs() {
        List<PermissionDTO> permissionDTOs = List.of(testPermissionDTO);

        when(permissionService.getAllPermissions()).thenReturn(new ResponseEntity<>(permissionDTOs, HttpStatus.OK));

        ResponseEntity<List<PermissionDTO>> response = permissionController.getAllPermissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permissionDTOs, response.getBody());
    }

    @Test
    void getPermissionByName_ShouldReturnPermissionDTO_WhenPermissionExists() {
        when(permissionService.getPermissionByName("READ_PRIVILEGES")).thenReturn(new ResponseEntity<>(testPermissionDTO, HttpStatus.OK));

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionByName("READ_PRIVILEGES");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void getPermissionByName_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionService.getPermissionByName("READ_PRIVILEGES")).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionByName("READ_PRIVILEGES");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createPermission_ShouldReturnCreatedPermissionDTO() {
        when(permissionService.createPermission(testPermissionDTO)).thenReturn(new ResponseEntity<>(testPermissionDTO, HttpStatus.CREATED));

        ResponseEntity<PermissionDTO> response = permissionController.createPermission(testPermissionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void updatePermission_ShouldReturnUpdatedPermissionDTO_WhenPermissionExists() {
        when(permissionService.updatePermission(1L, testPermissionDTO)).thenReturn(new ResponseEntity<>(testPermissionDTO, HttpStatus.OK));

        ResponseEntity<PermissionDTO> response = permissionController.updatePermission(1L, testPermissionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void updatePermission_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionService.updatePermission(1L, testPermissionDTO)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<PermissionDTO> response = permissionController.updatePermission(1L, testPermissionDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePermission_ShouldReturnNoContent_WhenPermissionExists() {
        when(permissionService.deletePermission(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = permissionController.deletePermission(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletePermission_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionService.deletePermission(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<Void> response = permissionController.deletePermission(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}