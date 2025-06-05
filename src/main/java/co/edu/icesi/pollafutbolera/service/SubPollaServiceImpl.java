package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.exception.NoSubPollasForPollasException;
import co.edu.icesi.pollafutbolera.exception.SubPollaCreationException;
import co.edu.icesi.pollafutbolera.exception.SubPollaNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.SubPollaMapper;
import co.edu.icesi.pollafutbolera.model.SubPolla;
import co.edu.icesi.pollafutbolera.repository.SubPollaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SubPollaServiceImpl implements SubPollaService {

    private final SubPollaRepository repository;

    private final SubPollaMapper mapper;

    @Override
    public ResponseEntity<SubPollaGetDTO> findById(Long id) {
        try{
            SubPolla subPolla = repository.findById(id).get();
            return ResponseEntity.ok(mapper.toDTO(subPolla));
        }
        catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<SubPollaGetDTO> save(SubPollaCreateDTO subPollaCreateDTO) {
        try{
            SubPolla subPolla = mapper.toEntity(subPollaCreateDTO);
            SubPolla savedSubPolla = repository.save(subPolla);
            return ResponseEntity.ok(mapper.toDTO(savedSubPolla));
        }
        catch (IllegalArgumentException e){
            throw  new SubPollaCreationException("Error al guardar la subpolla, el objeto no puede ser nulo: " + e.getMessage());
        }
        catch (DataIntegrityViolationException e){
            throw new SubPollaCreationException("Error al guardar la subpolla: " + e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<SubPollaGetDTO>> findByPollaId(Long id) {
        try{
            List<SubPolla> subPolla = repository.findByPollaId(id);
            if (subPolla.isEmpty()) {
                throw new NoSuchElementException("No existen subpollas para la polla con ID " + id);
            }
            return ResponseEntity.ok(mapper.toDTOs(subPolla));
        }
        catch (NoSuchElementException e){
            throw new NoSubPollasForPollasException(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<SubPollaGetDTO>> findAll() {
        try {
            List<SubPolla> subPollas = repository.findAll();
            if (subPollas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(mapper.toDTOs(subPollas));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteSubPolla(Long id) {
        try {
            repository.findById(id).get();
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        catch(NoSuchElementException e){
            throw new SubPollaNotFoundException("No existe la subpolla con ID " + id);
        }
    }

    @Override
    public ResponseEntity<List<SubPollaGetDTO>> findByCreatorUserId(Long creatorUserId) {
        try {
            List<SubPolla> subPollas = repository.findByUserId(creatorUserId);
            if (subPollas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(mapper.toDTOs(subPollas));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}