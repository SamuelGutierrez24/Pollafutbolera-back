package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;
import co.edu.icesi.pollafutbolera.model.PlatformConfig;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import co.edu.icesi.pollafutbolera.service.PollaServiceImpl;
import co.edu.icesi.pollafutbolera.service.RewardService;
import co.edu.icesi.pollafutbolera.util.CompanyUtil;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PollaServiceImplTest {

    @Mock
    private PollaRepository pollaRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private PollaMapper pollaMapper;

    @Mock
    private RewardService rewardService;

    @Mock
    private RewardMapper rewardMapper;

    @InjectMocks
    private PollaServiceImpl pollaService;

    private Polla polla;
    private PollaConfigDTO pollaDTO;
    private Tournament tournament;
    private Tournament tournament2;
    private PlatformConfig platformConfig;
    private TournamentDTO tournamentConfigDTO;

    @BeforeEach
    void setUp() {
        tournament = Tournament.builder()
                .id(10L)
                .name("Champions League")
                .description("Top European competition")
                .build();

        tournament2 = Tournament.builder()
                .id(20L)
                .name("Eurocopa")
                .description("Competition")
                .build();

        platformConfig = PlatformConfig.builder()
                .id(2L)
                .tournamentChampion(1)
                .teamWithMostGoals(1)
                .exactScore(1)
                .matchWinner(1)
                .build();

        polla = Polla.builder()
                .id(1L)
                .startDate(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()))
                .endDate(Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()))
                .isPrivate(false)
                .imageUrl("test.jpg")
                .color("black")
                .platformConfig(platformConfig)
                .tournament(tournament)
                .build();

        PlatformConfigDTO platformConfigDTO = new PlatformConfigDTO(
                platformConfig.getId(),
                platformConfig.getTournamentChampion(),
                platformConfig.getTeamWithMostGoals()+1,
                platformConfig.getExactScore()+1,
                platformConfig.getMatchWinner()+1
        );

        TournamentDTO tournamentConfigDTO  = new TournamentDTO(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                LocalDate.now(),
                LocalDate.now(),
                10L,
                10L,
                10L,
                null
        );

        pollaDTO = new PollaConfigDTO(

                Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()),
                true,
                "blue",
                "newImage.jpg",
                platformConfigDTO,
                tournamentConfigDTO.getId(),
                List.of(
                        new RewardDTO(1L, "First Prize", "First place prize", "image1.jpg", 1, 5L),
                        new RewardDTO(2L, "Second Prize", "Second place prize", "image2.jpg", 2, 5L)
                ),
                CompanyUtil.companyDTO().id()
        );
    }

    @Test
    void updatePolla_Success() {

        pollaDTO = PollaUtil.pollaConfigDTO;
        polla = PollaUtil.polla();


        when(pollaMapper.toPolla(pollaDTO)).thenReturn(polla);
        when(pollaRepository.save(polla)).thenReturn(polla);
        when(pollaMapper.toPollaGetDTO(polla)).thenReturn(PollaUtil.pollaGetDTO);
        pollaService.updatePolla(1L, pollaDTO);


        verify(pollaRepository, times(1)).save(polla);
    }





}