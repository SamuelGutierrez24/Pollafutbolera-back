package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.exception.PollaNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ScoresEnd {

    private final PollaRepository pollaRepository;

    private final PollaMapper pollaMapper;

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
}
