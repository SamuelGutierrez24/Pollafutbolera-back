package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.StageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stages")
public interface StageApi {

    @GetMapping
    ResponseEntity<List<StageDTO>> findAll();

    @PostMapping
    ResponseEntity<StageDTO> createStage(@RequestBody StageDTO stageDTO);

    @GetMapping("/{id}")
    ResponseEntity<StageDTO> findById(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<StageDTO> update(@PathVariable Long id, @RequestBody StageDTO stageDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @GetMapping("/api/all")
    ResponseEntity<List<StageDTO>> getAllStagesApi(@RequestParam Long league, @RequestParam int season);

    @GetMapping("/tournament/{tournamentId}")
    ResponseEntity<List<StageDTO>> getStagesByTournament(@PathVariable("tournamentId") Long tournamentId);

    @GetMapping("/tournament/{tournamentId}/current")
    ResponseEntity<String> getCurrentStageName(@PathVariable("tournamentId") Long tournamentId);
}
