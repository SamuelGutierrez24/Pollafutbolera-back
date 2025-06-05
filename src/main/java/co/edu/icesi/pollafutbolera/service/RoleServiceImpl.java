package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.mapper.RoleMapper;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public ResponseEntity<RoleDTO> getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            RoleDTO roleDTO = roleMapper.toDTO(role.get());
            return ResponseEntity.ok(roleDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOs = roleMapper.toDTOList(roles);
        return ResponseEntity.ok(roleDTOs);
    }

    @Override
    public ResponseEntity<RoleDTO> getRoleByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isPresent()) {
            RoleDTO roleDTO = roleMapper.toDTO(role.get());
            return ResponseEntity.ok(roleDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<RoleDTO> createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        RoleDTO createdRoleDTO = roleMapper.toDTO(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoleDTO);
    }

    @Override
    public ResponseEntity<RoleDTO> updateRole(Long id, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            roleMapper.updateFromDTO(roleDTO, role);
            role = roleRepository.save(role);
            RoleDTO updatedRoleDTO = roleMapper.toDTO(role);
            return ResponseEntity.ok(updatedRoleDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}