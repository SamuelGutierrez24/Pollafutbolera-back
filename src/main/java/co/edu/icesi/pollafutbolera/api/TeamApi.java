package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/teams")
public interface TeamApi {

    @GetMapping
    ResponseEntity<List<TeamDTO>> findAll();

    @PostMapping
    ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO);

    @GetMapping("/{id}")
    ResponseEntity<TeamDTO> findById(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<TeamDTO> update(@PathVariable Long id, @RequestBody TeamDTO teamDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
