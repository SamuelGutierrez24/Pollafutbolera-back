package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubPollaService {

    ResponseEntity<SubPollaGetDTO> findById(Long id);
    ResponseEntity<SubPollaGetDTO> save(SubPollaCreateDTO subPollaCreateDTO);
    ResponseEntity<List<SubPollaGetDTO>> findByPollaId(Long id);
    ResponseEntity<List<SubPollaGetDTO>> findAll();
    ResponseEntity<Void> deleteSubPolla(Long id);
    ResponseEntity<List<SubPollaGetDTO>> findByCreatorUserId(Long creatorUserId);


}