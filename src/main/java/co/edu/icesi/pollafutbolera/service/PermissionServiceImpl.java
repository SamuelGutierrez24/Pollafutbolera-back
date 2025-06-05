package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.mapper.PermissionMapper;
import co.edu.icesi.pollafutbolera.model.Permission;
import co.edu.icesi.pollafutbolera.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public ResponseEntity<PermissionDTO> getPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if (permission.isPresent()) {
            PermissionDTO permissionDTO = permissionMapper.toDTO(permission.get());
            return ResponseEntity.ok(permissionDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionDTO> permissionDTOs = permissionMapper.toDTOList(permissions);
        return ResponseEntity.ok(permissionDTOs);
    }

    @Override
    public ResponseEntity<PermissionDTO> getPermissionByName(String name) {
        Optional<Permission> permission = permissionRepository.findByName(name);
        if (permission.isPresent()) {
            PermissionDTO permissionDTO = permissionMapper.toDTO(permission.get());
            return ResponseEntity.ok(permissionDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<PermissionDTO> createPermission(PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        PermissionDTO createdPermissionDTO = permissionMapper.toDTO(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermissionDTO);
    }

    @Override
    public ResponseEntity<PermissionDTO> updatePermission(Long id, PermissionDTO permissionDTO) {
        Optional<Permission> optionalPermission = permissionRepository.findById(id);
        if (optionalPermission.isPresent()) {
            Permission permission = optionalPermission.get();
            permission.setName(permissionDTO.name());
            permission = permissionRepository.save(permission);
            PermissionDTO updatedPermissionDTO = permissionMapper.toDTO(permission);
            return ResponseEntity.ok(updatedPermissionDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> deletePermission(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}