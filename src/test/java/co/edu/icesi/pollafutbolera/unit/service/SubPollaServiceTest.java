package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.exception.SubPollaCreationException;
import co.edu.icesi.pollafutbolera.mapper.SubPollaMapper;
import co.edu.icesi.pollafutbolera.model.SubPolla;
import co.edu.icesi.pollafutbolera.repository.SubPollaRepository;
import co.edu.icesi.pollafutbolera.service.SubPollaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubPollaServiceTest {

    @Mock
    private SubPollaRepository repository;

    @Mock
    private SubPollaMapper mapper;

    @InjectMocks
    private SubPollaServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        SubPolla subPolla = new SubPolla();
        subPolla.setId(1L);
        SubPollaGetDTO dto = SubPollaGetDTO.builder().id(1L).isPrivate(true).pollaId(1L).build();

        when(repository.findById(1L)).thenReturn(Optional.of(subPolla));
        when(mapper.toDTO(subPolla)).thenReturn(dto);

        ResponseEntity<SubPollaGetDTO> response = service.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<SubPollaGetDTO> response = service.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSave_Success() {
        SubPollaCreateDTO createDTO = SubPollaCreateDTO.builder().isPrivate(true).pollaId(1L).build();
        SubPolla subPolla = new SubPolla();
        SubPolla savedSubPolla = new SubPolla();
        SubPollaGetDTO dto = SubPollaGetDTO.builder().id(1L).isPrivate(true).pollaId(1L).build();

        when(mapper.toEntity(createDTO)).thenReturn(subPolla);
        when(repository.save(subPolla)).thenReturn(savedSubPolla);
        when(mapper.toDTO(savedSubPolla)).thenReturn(dto);

        ResponseEntity<SubPollaGetDTO> response = service.save(createDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testSave_BadRequest() {
        SubPollaCreateDTO createDTO = SubPollaCreateDTO.builder().isPrivate(true).pollaId(1L).build();

        when(mapper.toEntity(createDTO)).thenThrow(new IllegalArgumentException("Invalid data"));

        try {
            ResponseEntity<SubPollaGetDTO> response = service.save(createDTO);
            assert false;
        }
        catch (SubPollaCreationException e) {
            assert true;
        }
    }

    @Test
    void testDelete_Success(){
        Long id = 1L;
        SubPolla subPolla = new SubPolla();
        subPolla.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(subPolla));
        doNothing().when(repository).deleteById(id);

        ResponseEntity<Void> response = service.deleteSubPolla(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_NotFound(){
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        try{
            ResponseEntity<Void> response = service.deleteSubPolla(id);
            assert false;
        }
        catch (RuntimeException e) {
            assert true;
        }
        verify(repository, never()).deleteById(id);
    }
}