package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.FixtureController;
import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import co.edu.icesi.pollafutbolera.service.FixtureService;
import co.edu.icesi.pollafutbolera.service.FixtureServiceImpl;
import co.edu.icesi.pollafutbolera.util.FixtureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FixtureControllerTest {

    @Mock
    private FixtureServiceImpl fixtureService;

    @InjectMocks
    private FixtureController fixtureController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStadistics() throws Exception {
        Long fixtureId = 11345L;
        List<FixtureDTO> fixtureDTO = FixtureUtil.fixture();

        when(fixtureService.fixtureStatics(fixtureId)).thenReturn(fixtureDTO) ;

        ResponseEntity<?> response = fixtureController.getStatidistics(fixtureId);

        assertFalse(response.toString().isEmpty());
        assertEquals(ResponseEntity.ok(fixtureDTO), response);
    }
}
