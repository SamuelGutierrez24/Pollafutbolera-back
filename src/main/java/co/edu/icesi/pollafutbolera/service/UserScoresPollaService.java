package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.RankingDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.dto.UserScoresPollaDTO;
import co.edu.icesi.pollafutbolera.exception.PollaNotFoundException;
import co.edu.icesi.pollafutbolera.exception.UserNotInPollaException;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserScoresPollaService {

    ResponseEntity<List<UserDTO>> getUsersByPollaId(Long pollaId) throws PollaNotFoundException;

    ResponseEntity<UserScoresPollaDTO> findByUserIdAndPollaId(Long userId, Long pollaId) throws UserNotInPollaException;

    ResponseEntity<Boolean> banUserFromPolla(Long userId, Long pollaId);

    ResponseEntity<List<RankingDTO>> rankingByPolla(Long pollaId, Pageable pageable);

    ResponseEntity<Boolean> updateUserScoresByPolla(Long pollaId);

    ResponseEntity<Boolean> updateUserScoresByMatch(Long matchId);

    ResponseEntity<List<Long>> getPollaIdsByUserId(Long userId);

    ResponseEntity<List<PollaGetDTO>> getPollasByUserId(String token);

    ResponseEntity<Boolean> createRelation(Long userId, Long pollaId);
    
    ResponseEntity<List<RankingDTO>> rankingBySubPolla(Long subPollaId, Pageable pageable);

    ResponseEntity<Long> findRankingByUserIdAndPollaId(Long userId, Long pollaId) throws UserNotInPollaException;
    ResponseEntity<Long> findRankingSubPolla(Long userId, Long subPollaId) throws UserNotInPollaException;

}
