package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.mapper.PermissionMapper;
import co.edu.icesi.pollafutbolera.model.Permission;
import co.edu.icesi.pollafutbolera.repository.PermissionRepository;
import co.edu.icesi.pollafutbolera.service.PermissionServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission testPermission;
    private PermissionDTO testPermissionDTO;

    @BeforeEach
    void setUp() {
        testPermission = Permission.builder()
                .id(1L)
                .name("READ_PRIVILEGES")
                .build();

        testPermissionDTO = PermissionDTO.builder()
                .id(1L)
                .name("READ_PRIVILEGES")
                .build();
    }

    @Test
    void getPermissionById_ShouldReturnPermissionDTO_WhenPermissionExists() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(testPermission));
        when(permissionMapper.toDTO(testPermission)).thenReturn(testPermissionDTO);

        ResponseEntity<PermissionDTO> response = permissionService.getPermissionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void getPermissionById_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<PermissionDTO> response = permissionService.getPermissionById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllPermissions_ShouldReturnListOfPermissionDTOs() {
        List<Permission> permissions = List.of(testPermission);
        List<PermissionDTO> permissionDTOs = List.of(testPermissionDTO);

        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.toDTOList(permissions)).thenReturn(permissionDTOs);

        ResponseEntity<List<PermissionDTO>> response = permissionService.getAllPermissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permissionDTOs, response.getBody());
    }

    @Test
    void getPermissionByName_ShouldReturnPermissionDTO_WhenPermissionExists() {
        when(permissionRepository.findByName("READ_PRIVILEGES")).thenReturn(Optional.of(testPermission));
        when(permissionMapper.toDTO(testPermission)).thenReturn(testPermissionDTO);

        ResponseEntity<PermissionDTO> response = permissionService.getPermissionByName("READ_PRIVILEGES");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void getPermissionByName_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionRepository.findByName("READ_PRIVILEGES")).thenReturn(Optional.empty());

        ResponseEntity<PermissionDTO> response = permissionService.getPermissionByName("READ_PRIVILEGES");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createPermission_ShouldReturnCreatedPermissionDTO() {
        when(permissionMapper.toEntity(testPermissionDTO)).thenReturn(testPermission);
        when(permissionRepository.save(testPermission)).thenReturn(testPermission);
        when(permissionMapper.toDTO(testPermission)).thenReturn(testPermissionDTO);

        ResponseEntity<PermissionDTO> response = permissionService.createPermission(testPermissionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void updatePermission_ShouldReturnUpdatedPermissionDTO_WhenPermissionExists() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(testPermission));
        when(permissionRepository.save(testPermission)).thenReturn(testPermission);
        when(permissionMapper.toDTO(testPermission)).thenReturn(testPermissionDTO);

        ResponseEntity<PermissionDTO> response = permissionService.updatePermission(1L, testPermissionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPermissionDTO, response.getBody());
    }

    @Test
    void updatePermission_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<PermissionDTO> response = permissionService.updatePermission(1L, testPermissionDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePermission_ShouldReturnNoContent_WhenPermissionExists() {
        when(permissionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(permissionRepository).deleteById(1L);

        ResponseEntity<Void> response = permissionService.deletePermission(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletePermission_ShouldReturnNotFound_WhenPermissionDoesNotExist() {
        when(permissionRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = permissionService.deletePermission(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}