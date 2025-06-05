// src/test/java/co/edu/icesi/pollafutbolera/unit/controller/PollaControllerTest.java
package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.service.PollaService;
import co.edu.icesi.pollafutbolera.controller.PollaController;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class PollaControllerTest {

    @Mock
    private PollaService pollaService;

    @InjectMocks
    private PollaController pollaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPolla() {
        Long pollaId = 1L;
        PollaGetDTO pollaGetDTO = PollaUtil.pollaGetDTO();

        when(pollaService.findPollaById(pollaId)).thenReturn(ResponseEntity.ok(pollaGetDTO)) ;

        ResponseEntity<PollaGetDTO> response = pollaController.getPolla(pollaId);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(pollaGetDTO), response);
        verify(pollaService, times(1)).findPollaById(pollaId);
    }

    @Test
    public void testSavePolla() {
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;

        pollaController.savePolla(pollaConfigDTO);

        verify(pollaService, times(1)).savePolla(pollaConfigDTO);
    }

    @Test
    public void testUpdatePolla() {
        Long pollaId = 1L;
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;


        pollaController.updatePolla(pollaId, pollaConfigDTO);

        verify(pollaService, times(1)).updatePolla(pollaId, pollaConfigDTO);
    }

    @Test
    public void deletePolla() {
        Long pollaId = 1L;

        pollaController.deletePolla(pollaId);

        verify(pollaService, times(1)).deletePolla(pollaId);
    }
}