package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.exception.PollaNotFoundException;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;

import co.edu.icesi.pollafutbolera.model.PlatformConfig;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Reward;
import co.edu.icesi.pollafutbolera.model.Tournament;

import co.edu.icesi.pollafutbolera.model.*;

import co.edu.icesi.pollafutbolera.repository.PlatformConfigRepository;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.RewardRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;



@Service
@RequiredArgsConstructor
public class PollaServiceImpl implements PollaService {

    private final PollaRepository pollaRepository;
    private final PollaMapper pollaMapper;
    private final UserScoresPollaService userScoresPollaService;
    private final RewardMapper rewardMapper;
    private final TournamentRepository tournamentRepository;

    private final RewardRepository rewardRepository;

    private final UserScoresPollaRepository userScoresPollaRepository;

    private final PlatformConfigRepository platformConfigRepository;


    @Override
    public ResponseEntity<PollaGetDTO> savePolla(PollaConfigDTO polla) {
        try{
            Polla pollaSaved = pollaMapper.toPolla(polla);
            //Rewards should not be saved with the polla
            pollaSaved.setRewards(null);
            pollaSaved.setIsActive(true);
            return ResponseEntity.ok(pollaMapper.toPollaGetDTO(pollaRepository.save(pollaSaved)));
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    @Transactional()
    public void deletePolla(Long id) {
        Polla polla = pollaRepository.findById(id)
                .orElseThrow(() -> new PollaNotFoundException("Polla con ID " + id + " no encontrada"));



        pollaRepository.deleteById(id);

    }

    public ResponseEntity<List<UserScoresPolla>> endPolla(Long id){
        Polla polla = pollaRepository.findById(id)
                .orElseThrow(() -> new PollaNotFoundException("Polla con ID " + id + " no encontrada"));

        userScoresPollaService.updateUserScoresByPolla(id);

        List<UserScoresPolla> top10 = userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(id, PageRequest.of(0, 10));

        polla.setIsActive(false);

        pollaRepository.save(polla);



        return ResponseEntity.ok(top10);
    }

    @Transactional
    @Override
    public ResponseEntity<PollaGetDTO> updatePolla(Long id, PollaConfigDTO pollaDTO) {
        try{


            Polla updatedPolla = pollaMapper.toPolla(pollaDTO);
            updatedPolla.setId(id);

            updatedPolla.setRewards(null);
            if (updatedPolla.getStartDate().before(new Date())) {
                throw new IllegalStateException("No se puede editar una polla que ya ha iniciado.");
            }

            pollaRepository.save(updatedPolla);

            List<Reward> rewards = updatedPolla.getRewards();
            if (rewards != null) {
                for (Reward reward : rewards) {
                    reward.setPolla(updatedPolla);
                }
                rewardRepository.saveAll(rewards);
            }

            return ResponseEntity.ok(pollaMapper.toPollaGetDTO(updatedPolla));
        }
        catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }


    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PollaGetDTO[]> findAllPollas() {
        try{
            return ResponseEntity.ok(pollaRepository.findAll().stream()
                    .map(pollaMapper::toPollaGetDTO)
                    .toArray(PollaGetDTO[]::new));
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PollaGetDTO> findPollaById(Long id) {

        try{
            Polla polla = pollaRepository.findById(id).get();
            return ResponseEntity.ok(pollaMapper.toPollaGetDTO(polla));
        }
        catch (NoSuchElementException e){
            throw new PollaNotFoundException("The polla with id " + id + " was not found");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public void findPollaByName(String name) {

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PollaGetDTO[]> findPollasByCompanyId(Long id) {
        return ResponseEntity.ok(pollaRepository.findbyCompanyId(id).stream()
                .map(pollaMapper::toPollaGetDTO)
                .toArray(PollaGetDTO[]::new));
    }
}
