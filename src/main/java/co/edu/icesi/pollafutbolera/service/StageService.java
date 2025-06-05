package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Team;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StageService extends GenericService<StageDTO, Long> {
    ResponseEntity<StageDTO> createStage(StageDTO stageDTO);
    ResponseEntity<List<StageDTO>> getAllStagesApi(Long league, int season);
    ResponseEntity<List<StageDTO>> getStagesByTournament(Long tournamentId);
    String getCurrentStageName(Long tournamentId);


}