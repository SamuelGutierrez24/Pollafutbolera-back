package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RewardService {

    ResponseEntity<RewardDTO> findById(Long id);
    ResponseEntity<RewardDTO[]> saveAll(List<RewardSaveDTO> rewardDTO);
    void deleteById(Long id);
    ResponseEntity<RewardDTO[]> findByPollaId(Long pollaId);
    ResponseEntity<RewardDTO[]> update(List<RewardDTO> rewardDTO);



}
