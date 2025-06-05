package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.JwtAuthFilter;
import co.edu.icesi.pollafutbolera.controller.MatchRestController;
import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.exception.MatchNotFoundException;
import co.edu.icesi.pollafutbolera.service.JwtService;
import co.edu.icesi.pollafutbolera.service.MatchService;
import co.edu.icesi.pollafutbolera.service.StageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    @MockitoBean
    private StageService stageService;

    private MatchDTO matchDTO;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {



        matchDTO = new MatchDTO(
                978072L,
                LocalDateTime.of(2022, 12, 9, 15, 0),
                "Match Finished",
                24L,
                37L,
                24L,
                3L,
                11L,
                null,
                1,
                1,
                false,
                null,
                null,
                false,
                null,
                null

        );

    }

    @Test
    void testFindAll_Success() throws Exception {

        when(matchService.findAll()).thenReturn(ResponseEntity.ok(List.of(matchDTO)));

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(978072L));
    }

    @Test
    void testFindAll_EmptyList() throws Exception {
        when(matchService.findAll()).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void testFindAll_Exception() throws Exception {
        when(matchService.findAll()).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateMatch_Success() throws Exception {
        when(matchService.createMatch(Mockito.any(MatchDTO.class)))
                .thenReturn(ResponseEntity.status(201).body(matchDTO));

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matchDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(978072L));
    }

/* 
    TEST TO DO 
    
    @Test
    void testCreateMatch_BadRequest() throws Exception {

        MatchDTO invalidMatch = new MatchDTO(
        null, 
        null,  
        "",    
        null,  
        null,  
        null,  
        null,  
        null,  
        null   
        );

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMatch)))
                .andExpect(status().isBadRequest());
    }
*/          

    @Test
    void testFindById_Success() throws Exception {
        Long matchId = 1L;
        matchDTO = new MatchDTO(
                1L,
                LocalDateTime.of(2022, 12, 9, 15, 0),
                "Match Finished",
                2L,
                3L,
                4L,
                3L,
                1L,
                null,
                1,
                1,
                false,
                null,
                null,
                false,
                null,
                null
        );

        when(matchService.findById(matchId)).thenReturn(ResponseEntity.ok(matchDTO));

        mockMvc.perform(get("/api/matches/{id}", matchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(matchDTO.id()));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        Long matchId = 1L;

        when(matchService.findById(matchId)).thenThrow(new MatchNotFoundException());

        mockMvc.perform(get("/api/matches/{id}", matchId))
                .andExpect(status().isNotFound());
    }
 

    @Test
    void testFindByStage_Success() throws Exception {
        List<MatchDTO> matches = Arrays.asList(
            new MatchDTO(1L, LocalDateTime.of(2022, 12, 9, 15, 0), "Match Finished", 2L, 3L, 4L, 1L, 1L, null,1,
                    1,
                    false,
                    null,
                    null,
                    false,
                    null,
                    null),
            new MatchDTO(2L, LocalDateTime.of(2022, 12, 9, 15, 0), "Match Finished", 2L, 3L, 4L, 1L, 1L, null,1,
                    1,
                    false,
                    null,
                    null,
                    false,
                    null,
                    null)
        );

        when(matchService.findByStageAndTournament(1L, 1L))
                .thenReturn(ResponseEntity.ok(matches));

        mockMvc.perform(get("/api/matches/stage/{stage}/tournament/{tournament}", 1, 1)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(matches.size()));
    }


    @Test
    void testFindByStage_NotFound() throws Exception {
        Long stageId = 1L;
        Long tournamentId = 1L;

        when(matchService.findByStageAndTournament(stageId, tournamentId))
                .thenReturn(ResponseEntity.notFound().build());  // <-- Mejor forma de retornar NOT_FOUND

        mockMvc.perform(get("/api/matches/stage/{stage}/tournament/{tournament}", stageId, tournamentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByTournament_Success() throws Exception {
        Long tournamentId = 1L;

        List<MatchDTO> matches = Arrays.asList(
            new MatchDTO(1L, LocalDateTime.of(2022, 12, 9, 15, 0), "Match Finished", 2L, 3L, 4L, 1L, 1L, null,1,
                    1,
                    false,
                    null,
                    null,
                    false,
                    null,
                    null),
            new MatchDTO(2L, LocalDateTime.of(2022, 12, 10, 18, 0), "Match Finished", 5L, 6L, 5L, 1L, 2L, null,1,
                    1,
                    false,
                    null,
                    null,
                    false,
                    null,
                    null)
        );

        when(matchService.findByTournament(tournamentId))
                .thenReturn(ResponseEntity.ok(matches));

        mockMvc.perform(get("/api/matches/tournament/{tournament}", tournamentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(matches.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testFindByTournament_NotFound() throws Exception {
        Long tournamentId = 99L; // Un torneo que no existe

        when(matchService.findByTournament(tournamentId))
                .thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/matches/tournament/{tournament}", tournamentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_Success() throws Exception {
        Long matchId = 1L;
        MatchDTO matchDTO = new MatchDTO(matchId, 
                                        LocalDateTime.of(2022, 12, 9, 15, 0), 
                                        "Updated Status", 
                                        2L, 3L, 4L, 
                                        1L, 1L, null,1,
                1,
                false,
                null,
                null,
                false,
                null,
                null);

        objectMapper.registerModule(new JavaTimeModule());

        when(matchService.save(eq(matchId), any(MatchDTO.class)))
                .thenReturn(ResponseEntity.ok(matchDTO));

        mockMvc.perform(put("/api/matches/{id}", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(matchDTO.id()))
                .andExpect(jsonPath("$.status").value(matchDTO.status()));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        Long matchId = 999L; 
        MatchDTO matchDTO = new MatchDTO(matchId, 
                                        LocalDateTime.of(2022, 12, 9, 15, 0), 
                                        "Updated Status", 
                                        2L, 3L, 4L, 
                                        1L, 1L, null,1,
                1,
                false,
                null,
                null,
                false,
                null,
                null);

        objectMapper.registerModule(new JavaTimeModule());

        when(matchService.save(eq(matchId), any(MatchDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/matches/{id}", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_BadRequest() throws Exception {
        Long matchId = 1L;
        MatchDTO invalidMatchDTO = new MatchDTO(matchId, 
                                                null, 
                                                "", 
                                                null, null, null, 
                                                null, null, null,1,
                1,
                false,
                null,
                null,
                false,
                null,
                null);

        objectMapper.registerModule(new JavaTimeModule());

        when(matchService.save(eq(matchId), any(MatchDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mockMvc.perform(put("/api/matches/{id}", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMatchDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete_Success() throws Exception {
        Long matchId = 1L;

        // Simulamos que el servicio elimina sin problemas
        doNothing().when(matchService).deleteById(matchId);

        mockMvc.perform(delete("/api/matches/{id}", matchId))
                .andExpect(status().isNoContent());

        verify(matchService, times(1)).deleteById(matchId);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        Long matchId = 999L;

        // Simulamos que el servicio lanza una excepción al intentar eliminar un partido inexistente
        doThrow(new RuntimeException("Match not found")).when(matchService).deleteById(matchId);

        mockMvc.perform(delete("/api/matches/{id}", matchId))
                .andExpect(status().isNotFound());

        verify(matchService, times(1)).deleteById(matchId);
    }



}
