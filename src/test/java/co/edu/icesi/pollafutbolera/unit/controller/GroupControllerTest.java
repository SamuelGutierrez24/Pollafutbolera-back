package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.JwtAuthFilter;
import co.edu.icesi.pollafutbolera.controller.GroupController;
import co.edu.icesi.pollafutbolera.dto.GroupDTO;
import co.edu.icesi.pollafutbolera.service.GroupService;
import co.edu.icesi.pollafutbolera.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {
        "spring.sql.init.mode=never"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtService jwtService;


    private GroupDTO dummyGroup;

    @BeforeEach
    void setUp() {
        dummyGroup = GroupDTO.builder()
                .groupName("Group A")
                .tournamentId(1L)
                .build();
    }
    
    @Test
    void testRetrieveRawStandingsJson() throws Exception {
        GroupDTO dummyGroup = GroupDTO.builder()
                .groupName("Group A")
                .tournamentId(1L)
                .build();

        List<GroupDTO> groupList = List.of(dummyGroup);

        Mockito.when(groupService.retrieveStandingsAsDTOs(123, 2024))
                        .thenReturn(Mono.just(groupList));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/test/standings/raw")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].groupName").value("Group A"));
    }

    @Test
    void testRetrieveGroupStageFixtures_success() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode mockJson = mapper.readTree("""
            {
                "fixtures": [
                    {"matchId": 1, "teamA": "A", "teamB": "B"}
                ]
            }
        """);

        Mockito.when(groupService.retrieveGroupStageFixtures(123, 2024))
                .thenReturn(Mono.just(mockJson));

        mockMvc.perform(get("/api/groups/test/fixtures/raw")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fixtures").isArray())
                .andExpect(jsonPath("$.fixtures[0].matchId").value(1));
    }

    @Test
    void testRetrieveGroupStageFixtures_serviceError() throws Exception {
        Mockito.when(groupService.retrieveGroupStageFixtures(123, 2024))
                .thenReturn(Mono.error(new RuntimeException("External API error")));

        mockMvc.perform(get("/api/groups/test/fixtures/raw")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testRetrieveGroupStageFixtures_nullResponse() throws Exception {
        Mockito.when(groupService.retrieveGroupStageFixtures(123, 2024))
                .thenReturn(Mono.justOrEmpty(null)); 

        mockMvc.perform(get("/api/groups/test/fixtures/raw")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); 
    }

    @Test
    void testRetrieveAndSaveStandings_success() throws Exception {
        Mockito.when(groupService.retrieveAndSaveStandings(123, 2024))
                .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/groups/standings/save")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRetrieveAndSaveStandings_serviceError() throws Exception {
        Mockito.when(groupService.retrieveAndSaveStandings(123, 2024))
                .thenReturn(Mono.error(new RuntimeException("Error al guardar standings")));

        mockMvc.perform(get("/api/groups/standings/save")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testRetrieveAndSaveStandings_missingParams() throws Exception {
        mockMvc.perform(get("/api/groups/standings/save")
                        .header("X-TenantId", 10)
                        .param("league", "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRetrieveAndSaveGroupStageMatches_success() throws Exception {
        Mockito.when(groupService.retrieveAndSaveGroupStageMatches(123, 2024))
            .thenReturn(Mono.empty());

        mockMvc.perform(get("/api/groups/matches/save")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testRetrieveAndSaveGroupStageMatches_nullParams() throws Exception {
        mockMvc.perform(get("/api/groups/matches/save"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRetrieveAndSaveGroupStageMatches_serviceError() throws Exception {
        Mockito.when(groupService.retrieveAndSaveGroupStageMatches(123, 2024))
            .thenReturn(Mono.error(new RuntimeException("Error al guardar partidos")));

        mockMvc.perform(get("/api/groups/matches/save")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
            .andExpect(status().is5xxServerError());
    }

    @Test
    void testLinkMatchesToGroups_success() throws Exception {
        
        mockMvc.perform(get("/api/groups/matches/link").header("X-TenantId", 10))
            .andExpect(status().isOk());
    }

    @Test
    void testLinkMatchesToGroups_serviceError() throws Exception {
        Mockito.doThrow(new RuntimeException("Error al asociar partidos"))
            .when(groupService).linkMatchesToGroups();

        mockMvc.perform(get("/api/groups/matches/link").header("X-TenantId", 10))
            .andExpect(status().is5xxServerError());
    }

    @Test
    void testLinkMatchesToGroups_noParams() throws Exception {
        
        mockMvc.perform(get("/api/groups/matches/link").header("X-TenantId", 10))
            .andExpect(status().isOk());
    }

    @Test
    void testFindAllGroupsWithDetails_success() throws Exception {
        GroupDTO group1 = new GroupDTO(1L, "Group A", null, null, null, null, null);

        GroupDTO group2 = new GroupDTO(2L, "Group B", null, null, null, null, null);

        List<GroupDTO> mockGroups = Arrays.asList(group1, group2);

        Mockito.when(groupService.findAllGroupsWithDetails()).thenReturn(mockGroups);

        mockMvc.perform(get("/api/groups/details").header("X-TenantId", 10))

            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].groupName").value("Group A"))
            .andExpect(jsonPath("$[1].groupName").value("Group B"));
    }

    @Test
    void testFindAllGroupsWithDetails_serviceError() throws Exception {
        Mockito.when(groupService.findAllGroupsWithDetails())
            .thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(get("/api/groups/details").header("X-TenantId", 10))
            .andExpect(status().is5xxServerError());
    }

    @Test
    void testFindAllGroupsWithDetails_emptyList() throws Exception {
        Mockito.when(groupService.findAllGroupsWithDetails()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/groups/details").header("X-TenantId", 10))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0));
    }

    



    @Test
    void testGetGroupsByPolla_success() throws Exception {
        long pollaId = 10L;

        GroupDTO dto = GroupDTO.builder()
                .id(1L)
                .groupName("Group X")
                .tournamentId(1L)
                .build();

        Mockito.when(groupService.findGroupsByPolla(pollaId))
                .thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/by-polla/{pollaId}", pollaId).header("X-TenantId", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].groupName").value("Group X"));
    }

    @Test
    void testGetGroupsByPolla_notFound() throws Exception {
        long pollaId = 99L;

        Mockito.when(groupService.findGroupsByPolla(pollaId))
                .thenThrow(new IllegalArgumentException("Polla not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/by-polla/{pollaId}", pollaId).header("X-TenantId", 10))
                .andExpect(status().isNotFound());
    }





    @Test
    void testUpdateGroupStage_success() throws Exception {
        Mockito.when(groupService.updateGroupStage(123, 2024))
                .thenReturn(Mono.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/groups/update-group")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateGroupStage_serviceError() throws Exception {
        Mockito.when(groupService.updateGroupStage(123, 2024))
                .thenReturn(Mono.error(new RuntimeException("API error")));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/groups/update-group")
                        .header("X-TenantId", 10)
                        .param("league", "123")
                        .param("season", "2024"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testUpdateGroupStage_missingParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/groups/update-group")
                        .header("X-TenantId", 10)
                        .param("league", "123"))
                .andExpect(status().isBadRequest());
    }

}

