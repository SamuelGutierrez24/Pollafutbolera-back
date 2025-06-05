package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface TournamentBetService {
    ResponseEntity<TournamentBetDTO> getTournamentBetById(Long id);
    ResponseEntity<TournamentBetDTO> createTournamentBet(TournamentBetDTO tournamentBetDTO);
    ResponseEntity<TournamentBetDTO> updateTournamentBet(Long id, TournamentBetUpdateDTO updateTournamentBetDTO);
    ResponseEntity<Void> deleteTournamentBet(Long id);
    ResponseEntity<TournamentBetDTO> getTournamentBetByUserAndPolla(Long userId, Long pollaId);
    List<TournamentBet> getTournamentBetsByPolla(Long pollaId);
    void updateEarnedPointsByUserAndPolla(Long userId, Long pollaId, int earnedPoints);
}
