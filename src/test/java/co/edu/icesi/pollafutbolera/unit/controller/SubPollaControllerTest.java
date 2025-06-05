package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.controller.SubPollaController;
import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.service.SubPollaService;
import co.edu.icesi.pollafutbolera.util.SubPollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SubPollaControllerTest {

    @Mock
    private SubPollaService subPollaService;

    @InjectMocks
    private SubPollaController subPollaController;

    private List<SubPollaGetDTO> subPollaGetDTOs;
    private SubPollaGetDTO subPollaGetDTO;
    private SubPollaCreateDTO subPollaCreateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subPollaGetDTOs = SubPollaUtil.createSampleSubPollaGetDTOs();
        subPollaGetDTO = subPollaGetDTOs.get(0);
        subPollaCreateDTO = SubPollaUtil.createSampleSubPollaCreateDTOs().get(0);
    }

    @Test
    void testFindByPollaId() {
        when(subPollaService.findByPollaId(anyLong())).thenReturn(ResponseEntity.ok(subPollaGetDTOs));

        ResponseEntity<List<SubPollaGetDTO>> response = subPollaController.findByPollaId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subPollaGetDTOs, response.getBody());
        verify(subPollaService, times(1)).findByPollaId(1L);
    }

    @Test
    void testFindById() {
        when(subPollaService.findById(anyLong())).thenReturn(ResponseEntity.ok(subPollaGetDTO));

        ResponseEntity<SubPollaGetDTO> response = subPollaController.findById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subPollaGetDTO, response.getBody());
        verify(subPollaService, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(subPollaService.save(any(SubPollaCreateDTO.class))).thenReturn(ResponseEntity.ok(subPollaGetDTO));

        ResponseEntity<SubPollaGetDTO> response = subPollaController.save(subPollaCreateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subPollaGetDTO, response.getBody());
        verify(subPollaService, times(1)).save(subPollaCreateDTO);
    }

    @Test
    void testDelete() {
        when(subPollaService.deleteSubPolla(anyLong())).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = subPollaController.deleteSubPolla(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subPollaService, times(1)).deleteSubPolla(1L);
    }
}