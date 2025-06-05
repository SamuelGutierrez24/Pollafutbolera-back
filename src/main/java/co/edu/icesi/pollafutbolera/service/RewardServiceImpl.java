package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Reward;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.RewardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;

    private final RewardMapper rewardMapper;

    private final PollaRepository pollaRepository;

    @Override
    public ResponseEntity<RewardDTO> findById(Long id) {
        try{
            Reward reward = rewardRepository.findById(id).orElseThrow();
            return ResponseEntity.ok(rewardMapper.toRewardDTO(reward));
        }
        catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<RewardDTO[]> saveAll(List<RewardSaveDTO> rewardDTO) {
        try {
            Reward[] rewards = rewardMapper.toRewardsFromSaveDTO(rewardDTO.toArray(new RewardSaveDTO[0]));
            List<Reward> rewardsToSave = new ArrayList<>();

            for (Reward reward : rewards) {
                Polla polla = pollaRepository.getReferenceById(reward.getPolla().getId());
                reward.setPolla(polla);
                rewardsToSave.add(reward);
            }

            RewardDTO[] saved = rewardMapper.toRewardDTOs(
                    rewardRepository.saveAll(rewardsToSave).toArray(new Reward[0])
            );
            return ResponseEntity.ok(saved);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public void deleteById(Long id) {
    //TODO: ESTO
    }

    @Override
    public ResponseEntity<RewardDTO[]> findByPollaId(Long pollaId) {
        try{
            List<Reward> rewards = rewardRepository.findByPollaId(pollaId).get();
            RewardDTO[] rewardDTOS = rewardMapper.toRewardDTOs(rewards.toArray(new Reward[0]));
            return ResponseEntity.ok(rewardDTOS);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public ResponseEntity<RewardDTO[]> update(List<RewardDTO> rewardDTO) {
        try{
            Reward[] reward = rewardMapper.toRewards(rewardDTO.toArray(new RewardDTO[0]));
            rewardRepository.saveAll(List.of(reward));
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error al actualizar el premio: " + e.getMessage());
        }
        catch (OptimisticLockingFailureException e){
            throw new OptimisticLockingFailureException("Por lo menos uno de los premios no fueron encontrados: " + e.getMessage());
        }
        catch(Exception e){
            throw new RuntimeException("Error al actualizar el premio: " + e.getMessage());
        }
        return null;
    }
}
