package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.mapper.RoleMapper;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import co.edu.icesi.pollafutbolera.service.RoleServiceImpl;
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
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role testRole;
    private RoleDTO testRoleDTO;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
                .id(1L)
                .name("USER")
                .build();

        testRoleDTO = RoleDTO.builder()
                .id(1L)
                .name("USER")
                .build();
    }

    @Test
    void getRoleById_ShouldReturnRoleDTO_WhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleMapper.toDTO(testRole)).thenReturn(testRoleDTO);

        ResponseEntity<RoleDTO> response = roleService.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void getRoleById_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<RoleDTO> response = roleService.getRoleById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoleDTOs() {
        List<Role> roles = List.of(testRole);
        List<RoleDTO> roleDTOs = List.of(testRoleDTO);

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toDTOList(roles)).thenReturn(roleDTOs);

        ResponseEntity<List<RoleDTO>> response = roleService.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleDTOs, response.getBody());
    }

    @Test
    void getRoleByName_ShouldReturnRoleDTO_WhenRoleExists() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(testRole));
        when(roleMapper.toDTO(testRole)).thenReturn(testRoleDTO);

        ResponseEntity<RoleDTO> response = roleService.getRoleByName("USER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void getRoleByName_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        ResponseEntity<RoleDTO> response = roleService.getRoleByName("USER");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createRole_ShouldReturnCreatedRoleDTO() {
        when(roleMapper.toEntity(testRoleDTO)).thenReturn(testRole);
        when(roleRepository.save(testRole)).thenReturn(testRole);
        when(roleMapper.toDTO(testRole)).thenReturn(testRoleDTO);

        ResponseEntity<RoleDTO> response = roleService.createRole(testRoleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void updateRole_ShouldReturnUpdatedRoleDTO_WhenRoleExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(testRole)).thenReturn(testRole);
        when(roleMapper.toDTO(testRole)).thenReturn(testRoleDTO);

        ResponseEntity<RoleDTO> response = roleService.updateRole(1L, testRoleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoleDTO, response.getBody());
    }

    @Test
    void updateRole_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<RoleDTO> response = roleService.updateRole(1L, testRoleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteRole_ShouldReturnNoContent_WhenRoleExists() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1L);

        ResponseEntity<Void> response = roleService.deleteRole(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteRole_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = roleService.deleteRole(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}