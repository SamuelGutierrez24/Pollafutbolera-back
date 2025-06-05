package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permission")
public interface  PermissionAPI {

    @GetMapping("/{id}")
    ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<PermissionDTO>> getAllPermissions();

    @GetMapping("/name/{name}")
    ResponseEntity<PermissionDTO> getPermissionByName(@PathVariable String name);

    @PostMapping
    ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO);

    @PutMapping("/{id}")
    ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePermission(@PathVariable Long id);
}