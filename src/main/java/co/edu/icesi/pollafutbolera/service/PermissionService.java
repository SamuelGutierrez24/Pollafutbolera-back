package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface PermissionService {
    ResponseEntity<PermissionDTO> getPermissionById(Long id);
    ResponseEntity<List<PermissionDTO>> getAllPermissions();
    ResponseEntity<PermissionDTO> getPermissionByName(String name);
    ResponseEntity<PermissionDTO> createPermission(PermissionDTO permissionDTO);
    ResponseEntity<PermissionDTO> updatePermission(Long id, PermissionDTO permissionDTO);
    ResponseEntity<Void> deletePermission(Long id);
}