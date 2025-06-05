package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/matches")
public interface MatchApi {

    @GetMapping
    ResponseEntity<List<MatchDTO>> findAll();

    @PostMapping
    ResponseEntity<MatchDTO> createMatch(MatchDTO matchDTO);

    @GetMapping("/{id}")
    ResponseEntity<MatchDTO> findById(@PathVariable(name = "id") Long id);

    @GetMapping("/stage/{stage}/tournament/{tournament}")
    ResponseEntity<List<MatchDTO>> findByStage(@PathVariable(name = "stage") Long stage, @PathVariable(name = "tournament") Long tournament);

    @GetMapping("/tournament/{tournament}")
    ResponseEntity<List<MatchDTO>> findByTornament(@PathVariable(name = "tournament") Long tournament);

    @PutMapping("/{id}")
    ResponseEntity<MatchDTO> update(@PathVariable(name = "id") Long id, @RequestBody MatchDTO matchDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable(name = "id") Long id);
}
