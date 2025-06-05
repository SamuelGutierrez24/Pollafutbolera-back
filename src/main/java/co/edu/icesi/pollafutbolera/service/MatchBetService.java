package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchBetService {
    ResponseEntity<MatchBetResponseDTO> getMatchBetById(Long id);
    ResponseEntity<MatchBetResponseDTO> createMatchBet(MatchBetDTO matchBetDTO);
    ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUser(Long userId);
    ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUserAndPolla(Long userId, Long pollaId);
    ResponseEntity<MatchBetResponseDTO> updateMatchBet(Long id, MatchBetUpdateDTO updateMatchBetDTO);
    ResponseEntity<Void> deleteMatchBet(Long id);
    ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByMatch(Long matchId);
}
