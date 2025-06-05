package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
public interface RoleAPI {

    @GetMapping("/{id}")
    ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<RoleDTO>> getAllRoles();

    @GetMapping("/name/{name}")
    ResponseEntity<RoleDTO> getRoleByName(@PathVariable String name);

    @PostMapping
    ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO);

    @PutMapping("/{id}")
    ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRole(@PathVariable Long id);
}