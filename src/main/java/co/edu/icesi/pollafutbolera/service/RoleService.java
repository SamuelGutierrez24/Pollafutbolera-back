package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface RoleService {
    ResponseEntity<RoleDTO> getRoleById(Long id);
    ResponseEntity<List<RoleDTO>> getAllRoles();
    ResponseEntity<RoleDTO> getRoleByName(String name);
    ResponseEntity<RoleDTO> createRole(RoleDTO roleDTO);
    ResponseEntity<RoleDTO> updateRole(Long id, RoleDTO roleDTO);
    ResponseEntity<Void> deleteRole(Long id);
}