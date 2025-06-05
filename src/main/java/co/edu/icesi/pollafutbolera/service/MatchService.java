package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.Stage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MatchService extends GenericService<MatchDTO, Long> {

    ResponseEntity<MatchDTO> createMatch(MatchDTO matchDTO);
    ResponseEntity<List<MatchDTO>> findByStageAndTournament(Long stage, Long tournament) throws Exception;
    ResponseEntity<List<MatchDTO>> findByTournament(Long tournament) throws Exception;
    ResponseEntity<MatchDTO> apiMatchSave(MatchDTO matchDTO);
    ResponseEntity<List<MatchDTO>> findByTournamentAndDateBetween(Long tournamentId, LocalDateTime start, LocalDateTime end ) throws Exception;
}