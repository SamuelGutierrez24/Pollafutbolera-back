    package co.edu.icesi.pollafutbolera.unit.service;
    
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;

    import co.edu.icesi.pollafutbolera.model.*;
    import co.edu.icesi.pollafutbolera.repository.*;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.Mockito;
    import org.mockito.Spy;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.http.ResponseEntity;
    
    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.node.ArrayNode;
    import com.fasterxml.jackson.databind.node.ObjectNode;
    
    import co.edu.icesi.pollafutbolera.client.FootballApiClient;
    import co.edu.icesi.pollafutbolera.dto.GroupDTO;
    import co.edu.icesi.pollafutbolera.dto.GroupTeamDTO;
    import co.edu.icesi.pollafutbolera.dto.MatchDTO;
    import co.edu.icesi.pollafutbolera.mapper.GroupMapper;
    import co.edu.icesi.pollafutbolera.mapper.MatchMapper;
    import co.edu.icesi.pollafutbolera.service.GroupServiceImpl;
    import co.edu.icesi.pollafutbolera.service.MatchService;
    import reactor.core.publisher.Mono;
    
    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.anyInt;
    import static org.mockito.ArgumentMatchers.anyString;
    import static org.mockito.Mockito.*;
    
    import java.util.Optional;
    
    @ExtendWith(MockitoExtension.class)
    class GroupServiceTest {
    
        @Mock private FootballApiClient footballApiClient;
        @Mock private GroupRepository groupRepository;
        @Mock private TeamRepository teamRepository;
        @Mock private GroupTeamRepository groupTeamRepository;
        @Mock private GroupMatchRepository groupMatchRepository;
        @Mock private MatchRepository matchRepository;
        @Mock private StageRepository stageRepository;
        @Mock private PollaRepository pollaRepository;
        @Mock private GroupMapper groupMapper;
        @Mock private MatchMapper matchMapper;
        @Mock private MatchService matchService;
    
        @Spy
        @InjectMocks
        private GroupServiceImpl groupService;

        private void mockGroupStageNames() {
            List<Stage> stages = List.of(
                    Stage.builder().id(1L).name("Group Stage - 1").build(),
                    Stage.builder().id(2L).name("Group Stage - 2").build(),
                    Stage.builder().id(3L).name("Group Stage - 3").build()
            );

            when(stageRepository.findByTournament_IdAndNameContainingIgnoreCase(anyLong(), eq("group")))
                    .thenReturn(stages);
        }
    
        
    
        @Test
        void testRetrieveStandingsAsDTOs_success() {
            
            JsonNode mockJson = mock(JsonNode.class);
            List<GroupDTO> mockGroupDTOs = List.of(
                    new GroupDTO(1L, "Group A", null, null, null, null, null),
                    new GroupDTO(2L, "Group B", null, null, null, null, null)
            );
    
            when(footballApiClient.getStandings(123, 2024)).thenReturn(Mono.just(mockJson));
            when(groupMapper.fromStandingsJson(mockJson)).thenReturn(mockGroupDTOs);
    
            
            List<GroupDTO> result = groupService.retrieveStandingsAsDTOs(123, 2024).block();
    
            
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Group A", result.get(0).groupName());
            assertEquals("Group B", result.get(1).groupName());
    
            verify(footballApiClient).getStandings(123, 2024);
            verify(groupMapper).fromStandingsJson(mockJson);
        }
    
        @Test
        void testSaveGroupsInDatabase_success() {
    
            GroupTeamDTO teamDTO = GroupTeamDTO.builder()
                    .teamId(10L)
                    .teamName("Team 1")
                    .teamLogoUrl("logo1.png")
                    .rank(1)
                    .points(3)
                    .build();
    
            GroupDTO dto = GroupDTO.builder()
                    .id(1L)
                    .groupName("Group A")
                    .tournamentId(1L)
                    .teams(List.of(teamDTO))
                    .matches(null)
                    .firstWinnerTeamId(100L)
                    .secondWinnerTeamId(200L)
                    .build();
    
            when(groupRepository.findByGroupNameAndTournamentId("Group A", 1L))
                    .thenReturn(Optional.empty());
    
            Group savedGroup = Group.builder()
                    .id(1L)
                    .groupName("Group A")
                    .tournamentId(1L)
                    .build();
    
            when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
    
            Team team = Team.builder()
                    .id(10L)
                    .name("Team 1")
                    .logoUrl("logo1.png")
                    .build();
    
            when(teamRepository.findById(10L)).thenReturn(Optional.empty());
            when(teamRepository.save(any(Team.class))).thenReturn(team);
    
            
            groupService.saveGroupsInDatabase(List.of(dto)).block();
    
            
            verify(groupRepository).findByGroupNameAndTournamentId("Group A", 1L);
            verify(groupRepository).save(any(Group.class));
            verify(groupTeamRepository).save(any(GroupTeam.class));
            verify(teamRepository).save(any(Team.class)); 
        }
    
        @Test
        void testSaveGroupsInDatabase_groupAlreadyExists_shouldThrowException() {
            
            GroupDTO dto = GroupDTO.builder()
                    .id(1L)
                    .groupName("Group A")
                    .tournamentId(1L)
                    .teams(List.of())  
                    .matches(null)
                    .firstWinnerTeamId(100L)
                    .secondWinnerTeamId(200L)
                    .build();
        
            Group existingGroup = Group.builder()
                    .id(99L)
                    .groupName("Group A")
                    .tournamentId(1L)
                    .build();
        
            when(groupRepository.findByGroupNameAndTournamentId("Group A", 1L))
                    .thenReturn(Optional.of(List.of(existingGroup)));
        
            
            IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
                groupService.saveGroupsInDatabase(List.of(dto)).block();
            });
        
            assertEquals("A group named 'Group A' already exists in tournament 1", thrown.getMessage());
            verify(groupRepository).findByGroupNameAndTournamentId("Group A", 1L);
            verify(groupRepository, never()).save(any(Group.class));
            verify(groupTeamRepository, never()).save(any(GroupTeam.class));
        }
    
        
        @Test
        void testRetrieveAndSaveStandings_success() {
            int league = 123;
            int season = 2024;
        
            JsonNode mockJson = mock(JsonNode.class);
            List<GroupDTO> mockGroupDTOs = List.of(
                GroupDTO.builder()
                        .id(1L)
                        .groupName("Group A")
                        .tournamentId(1L)
                        .teams(List.of())
                        .build()
            );
        
            
            when(footballApiClient.getStandings(league, season)).thenReturn(Mono.just(mockJson));
            when(groupMapper.fromStandingsJson(mockJson)).thenReturn(mockGroupDTOs);
            when(groupRepository.findByGroupNameAndTournamentId("Group A", 1L)).thenReturn(Optional.empty());
        
            Group savedGroup = Group.builder().id(1L).groupName("Group A").tournamentId(1L).build();
            when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
        
            
            groupService.retrieveAndSaveStandings(league, season).block();
        
            
            verify(footballApiClient).getStandings(league, season);
            verify(groupMapper).fromStandingsJson(mockJson);
            verify(groupRepository).save(any(Group.class));
        }
        
        @Test
        void testRetrieveAndSaveStandings_retrieveFails() {
            int league = 123;
            int season = 2024;
        
            RuntimeException exception = new RuntimeException("API error");
        
            
            when(footballApiClient.getStandings(league, season)).thenReturn(Mono.error(exception));
        
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                groupService.retrieveAndSaveStandings(league, season).block();
            });
        
            assertEquals("API error", thrown.getMessage());
        
            verify(footballApiClient).getStandings(league, season);
            verify(groupMapper, never()).fromStandingsJson(any());
            verify(groupRepository, never()).save(any());
        }
    
        @Test
        void testRetrieveGroupStageFixtures_success() {
            mockGroupStageNames();

            int league = 123;
            int season = 2024;

            JsonNode fixture1 = new ObjectMapper().createObjectNode().put("round", "Group Stage - 1");
            JsonNode fixture2 = new ObjectMapper().createObjectNode().put("round", "Group Stage - 2");
            JsonNode fixture3 = new ObjectMapper().createObjectNode().put("round", "Group Stage - 3");

            when(footballApiClient.getFixtures(league, season, "Group Stage - 1")).thenReturn(Mono.just(fixture1));
            when(footballApiClient.getFixtures(league, season, "Group Stage - 2")).thenReturn(Mono.just(fixture2));
            when(footballApiClient.getFixtures(league, season, "Group Stage - 3")).thenReturn(Mono.just(fixture3));

            JsonNode result = groupService.retrieveGroupStageFixtures(league, season).block();

            assertNotNull(result);
            assertTrue(result.isArray());
            assertEquals(3, result.size());

            verify(footballApiClient).getFixtures(league, season, "Group Stage - 1");
            verify(footballApiClient).getFixtures(league, season, "Group Stage - 2");
            verify(footballApiClient).getFixtures(league, season, "Group Stage - 3");
        }

        @Test
        void testRetrieveGroupStageFixtures_oneFails() {
            mockGroupStageNames();
            int league = 123;
            int season = 2024;

            JsonNode fixture1 = new ObjectMapper().createObjectNode().put("round", "Group Stage - 1");
            JsonNode fixture2 = new ObjectMapper().createObjectNode().put("round", "Group Stage - 2");

            
            when(footballApiClient.getFixtures(league, season, "Group Stage - 1")).thenReturn(Mono.just(fixture1));
            when(footballApiClient.getFixtures(league, season, "Group Stage - 2")).thenReturn(Mono.just(fixture2));
            when(footballApiClient.getFixtures(league, season, "Group Stage - 3"))
                    .thenReturn(Mono.error(new RuntimeException("API failure")));

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                groupService.retrieveGroupStageFixtures(league, season).block();
            });

            assertEquals("API failure", thrown.getMessage());

            verify(footballApiClient).getFixtures(league, season, "Group Stage - 1");
            verify(footballApiClient).getFixtures(league, season, "Group Stage - 2");
            verify(footballApiClient).getFixtures(league, season, "Group Stage - 3");
        }

        @Test
        void testRetrieveAndSaveGroupStageMatches_success() {
            mockGroupStageNames();
            int league = 123;
            int season = 2024;

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode fixtureJson = mapper.createObjectNode().put("id", 1);
            ArrayNode responseArray = mapper.createArrayNode().add(fixtureJson);
            ObjectNode responseJson = mapper.createObjectNode().set("response", responseArray);

            when(footballApiClient.getFixtures(anyInt(), anyInt(), anyString()))
                    .thenReturn(Mono.just(responseJson));

            MatchDTO mockMatchDTO = MatchDTO.builder()
                    .id(1L)
                    .date(LocalDateTime.now())
                    .status("scheduled")
                    .homeTeamId(1L)
                    .awayTeamId(2L)
                    .tournamentId(100L)
                    .stageId(10L)
                    .homeScore(0)
                    .awayScore(0)
                    .extratime(false)
                    .penalty(false)
                    .build();

            when(matchMapper.fromJson(fixtureJson)).thenReturn(mockMatchDTO);
            when(matchService.createMatch(mockMatchDTO)).thenReturn(ResponseEntity.ok(mockMatchDTO));

            
            groupService.retrieveAndSaveGroupStageMatches(league, season).block();

            
            verify(footballApiClient, times(3)).getFixtures(anyInt(), anyInt(), anyString());
            verify(matchMapper, times(3)).fromJson(fixtureJson);
            verify(matchService, times(3)).createMatch(mockMatchDTO);
            
        }

        @Test
        void testRetrieveAndSaveGroupStageMatches_apiError() {
            mockGroupStageNames();
            int league = 123;
            int season = 2024;

            
            when(footballApiClient.getFixtures(anyInt(), anyInt(), anyString()))
                    .thenReturn(Mono.error(new RuntimeException("API failure")));

            
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                groupService.retrieveAndSaveGroupStageMatches(league, season).block();
            });

            assertEquals("API failure", thrown.getMessage());

            
            verify(footballApiClient).getFixtures(anyInt(), anyInt(), anyString());

            
            verify(matchMapper, never()).fromJson(any());
            verify(matchService, never()).createMatch(any());
        }

        @Test
        void testLinkMatchesToGroups_success() {
            
            Team homeTeam = Team.builder().id(1L).build();
            Match match = Match.builder().id(10L).homeTeam(homeTeam).build();
            Group group = Group.builder().id(100L).build();
    
            GroupTeam groupTeam = GroupTeam.builder()
                    .groupStage(group)
                    .team(homeTeam)
                    .build();
    
            when(matchRepository.findAll()).thenReturn(List.of(match));
            when(groupTeamRepository.findByTeamId(1L)).thenReturn(List.of(groupTeam));
            when(groupMatchRepository.save(any(GroupMatch.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
            
            groupService.linkMatchesToGroups();
    
            
            verify(matchRepository).findAll();
            verify(groupTeamRepository).findByTeamId(1L);
            verify(groupMatchRepository).save(any(GroupMatch.class));
        }
    
        @Test
        void testLinkMatchesToGroups_matchWithoutHomeTeam_isIgnored() {
            
            Match match = Match.builder().id(20L).homeTeam(null).build();
    
            when(matchRepository.findAll()).thenReturn(List.of(match));
    
            
            groupService.linkMatchesToGroups();
    
            
            verify(matchRepository).findAll();
            verifyNoInteractions(groupTeamRepository);
            verifyNoInteractions(groupMatchRepository);
        }
    
        @Test
        void testFindAllGroupsWithDetails_success() {
            
            Team team = Team.builder().id(1L).name("Team 1").build();
    
            GroupTeam team1 = GroupTeam.builder()
            .id(new GroupTeamId(1L, 1L)) 
            .rank(2)
            .team(team)
            .build();
    
            GroupTeam team2 = GroupTeam.builder()
                    .id(new GroupTeamId(1L, 2L))
                    .rank(1)
                    .team(team)
                    .build();
    
    
            Group group = Group.builder()
                    .id(1L)
                    .groupName("Group A")
                    .groupTeams(new HashSet<>(List.of(team1, team2)))
                    .build();
    
            when(groupRepository.findAllWithDetails())
                .thenReturn(new ArrayList<>(List.of(group))); 
    
    
            GroupDTO expectedDTO = GroupDTO.builder().id(1L).groupName("Group A").build();
            when(groupMapper.toGroupDTO(any(Group.class))).thenReturn(expectedDTO);
    
            
            List<GroupDTO> result = groupService.findAllGroupsWithDetails();
    
            
            assertEquals(1, result.size());
            assertEquals("Group A", result.get(0).groupName());
            verify(groupRepository).findAllWithDetails();
            verify(groupMapper).toGroupDTO(any(Group.class));
        }
    
        @Test
        void testFindAllGroupsWithDetails_failure() {
            
            when(groupRepository.findAllWithDetails())
                    .thenThrow(new RuntimeException("Database error"));
    
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                groupService.findAllGroupsWithDetails();
            });
    
            assertEquals("Database error", thrown.getMessage());
            verify(groupRepository).findAllWithDetails();
            verifyNoInteractions(groupMapper); 
        }



        @Test
        void testUpdateGroupStage_success() {
            int league = 123;
            int season = 2024;
            long tournamentId = league;

            
            Stage stage = Stage.builder().id(1L).name("Group Stage - 1").build();
            when(stageRepository
                    .findByTournament_IdAndNameContainingIgnoreCase(tournamentId, "group"))
                    .thenReturn(List.of(stage));

            
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode matchJson = mapper.createObjectNode().put("id", 1);
            ArrayNode respArr = mapper.createArrayNode().add(matchJson);
            ObjectNode fixturesJson = mapper.createObjectNode().set("response", respArr);

            when(footballApiClient.getFixtures(league, season, stage.getName()))
                    .thenReturn(Mono.just(fixturesJson));

            
            MatchDTO dto = MatchDTO.builder()
                    .id(1L).tournamentId(tournamentId)
                    .stageId(stage.getId())
                    .homeTeamId(10L).awayTeamId(20L)
                    .build();
            when(matchMapper.fromJson(matchJson)).thenReturn(dto);

            
            JsonNode standings = mapper.createObjectNode();
            when(footballApiClient.getStandings(league, season)).thenReturn(Mono.just(standings));
            when(groupMapper.fromStandingsJson(standings)).thenReturn(List.of());

            
            

            
            groupService.updateGroupStage(league, season).block();

            
            verify(footballApiClient).getFixtures(league, season, stage.getName());
            verify(matchMapper).fromJson(matchJson);
            verify(footballApiClient).getStandings(league, season);
        }





        @Test
        void testUpdateGroupStage_apiFailure_shouldPropagateError() {
            int league = 123;
            int season = 2024;
            Long tournamentId = (long) league;

            Stage stage = Stage.builder().id(1L).name("Group Stage - 1").build();
            when(stageRepository
                    .findByTournament_IdAndNameContainingIgnoreCase(tournamentId, "group"))
                    .thenReturn(List.of(stage));

            when(footballApiClient.getFixtures(league, season, stage.getName()))
                    .thenReturn(Mono.error(new RuntimeException("API error")));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> groupService.updateGroupStage(league, season).block());

            assertEquals("API error", ex.getMessage());
            verify(footballApiClient).getFixtures(league, season, stage.getName());
        }




        @Test
        void testFindGroupsByPolla_success() {
            long pollaId = 1L;

            Tournament tournament = Tournament.builder().id(100L).build();
            Polla polla = Polla.builder().id(pollaId).tournament(tournament).build();
            when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));

            Group group = Group.builder()
                    .id(1L).groupName("Group A")
                    .groupTeams(new HashSet<>())
                    .build();

            
            when(groupRepository.findAllWithDetailsByTournamentId(100L))
                    .thenReturn(new ArrayList<>(List.of(group)));

            GroupDTO dto = GroupDTO.builder().id(1L).groupName("Group A").build();
            when(groupMapper.toGroupDTO(group)).thenReturn(dto);

            List<GroupDTO> result = groupService.findGroupsByPolla(pollaId);

            assertEquals(1, result.size());
            assertEquals("Group A", result.get(0).groupName());

            verify(pollaRepository).findById(pollaId);
            verify(groupRepository).findAllWithDetailsByTournamentId(100L);
            verify(groupMapper).toGroupDTO(group);
        }





        @Test
        void testFindGroupsByPolla_notFound_shouldThrow() {
            Long pollaId = 99L;
            when(pollaRepository.findById(pollaId)).thenReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> groupService.findGroupsByPolla(pollaId));

            assertEquals("Polla not found", ex.getMessage());
            verify(pollaRepository).findById(pollaId);
            verifyNoInteractions(groupRepository);
        }




    }
