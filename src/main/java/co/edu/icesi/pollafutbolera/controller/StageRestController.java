package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.StageApi;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.service.StageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class StageRestController implements StageApi {

    private final StageService stageService;

    @Override
    public ResponseEntity<List<StageDTO>> findAll() {
        try {
            return stageService.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<StageDTO> createStage(@RequestBody StageDTO stageDTO) {
        try {
            return stageService.createStage(stageDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<StageDTO> findById(@PathVariable Long id) {
        try {
            return stageService.findById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StageDTO> update(@PathVariable Long id, @RequestBody StageDTO stageDTO) {
        try {
            return stageService.save(id, stageDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            stageService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<StageDTO>> getAllStagesApi(Long league,int season) {
        try {
            return stageService.getAllStagesApi(league, season);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<StageDTO>> getStagesByTournament(Long tournamentId) {
        return stageService.getStagesByTournament(tournamentId);
    }

    @Override
    public ResponseEntity<String> getCurrentStageName(@PathVariable Long tournamentId) {
        String stageName = stageService.getCurrentStageName(tournamentId);
        return ResponseEntity.ok(stageName);
    }

}

