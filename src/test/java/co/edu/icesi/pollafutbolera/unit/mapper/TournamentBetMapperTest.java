package co.edu.icesi.pollafutbolera.unit.mapper;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentBetMapper;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import co.edu.icesi.pollafutbolera.util.TournamentBetUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class TournamentBetMapperTest {

    TournamentBetMapper tournamentBetMapper = Mappers.getMapper(TournamentBetMapper.class);

    @Test
    public void testToTournamentBetDTO() {
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();

        TournamentBetDTO tournamentBetDTO = tournamentBetMapper.toDTO(tournamentBet);

        assertNotNull(tournamentBetDTO);
        assertEquals(tournamentBet.getId(), tournamentBetDTO.id());
        assertEquals(tournamentBet.getEarnedPoints(), tournamentBetDTO.earnedPoints());
        assertEquals(tournamentBet.getUser().getId(), tournamentBetDTO.userId());
        assertEquals(tournamentBet.getTournament().getId(), tournamentBetDTO.tournamentId());
        assertEquals(tournamentBet.getPolla().getId(), tournamentBetDTO.pollaId());
        assertEquals(tournamentBet.getWinnerTeam().getId(), tournamentBetDTO.winnerTeamId());
        assertEquals(tournamentBet.getTopScoringTeam().getId(), tournamentBetDTO.topScoringTeamId());
    }

    @Test
    public void testToTournamentBet() {
        TournamentBetDTO tournamentBetDTO = TournamentBetUtil.tournamentBetDTO();

        TournamentBet tournamentBet = tournamentBetMapper.toEntity(tournamentBetDTO);

        assertNotNull(tournamentBet);
        assertEquals(tournamentBetDTO.id(), tournamentBet.getId());
        assertEquals(tournamentBetDTO.earnedPoints(), tournamentBet.getEarnedPoints());
        assertEquals(tournamentBetDTO.userId(), tournamentBet.getUser().getId());
        assertEquals(tournamentBetDTO.tournamentId(), tournamentBet.getTournament().getId());
        assertEquals(tournamentBetDTO.pollaId(), tournamentBet.getPolla().getId());
        assertEquals(tournamentBetDTO.winnerTeamId(), tournamentBet.getWinnerTeam().getId());
        assertEquals(tournamentBetDTO.topScoringTeamId(), tournamentBet.getTopScoringTeam().getId());
    }

    @Test
    public void testUpdateTournamentBetFromDTO() {
        TournamentBetUpdateDTO updateDTO = TournamentBetUpdateDTO.builder()
                .winnerTeamId(2L)
                .topScoringTeamId(4L)
                .build();
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();

        tournamentBetMapper.updateTournamentBetFromDTO(updateDTO, tournamentBet);

        assertEquals(updateDTO.winnerTeamId(), tournamentBet.getWinnerTeam().getId());
        assertEquals(updateDTO.topScoringTeamId(), tournamentBet.getTopScoringTeam().getId());
    }

    @Test
    public void testToTournamentBetDTONull() {
        TournamentBet tournamentBet = null;

        TournamentBetDTO tournamentBetDTO = tournamentBetMapper.toDTO(tournamentBet);

        assertNull(tournamentBetDTO);
    }

    @Test
    public void testToTournamentBetNull() {
        TournamentBetDTO tournamentBetDTO = null;

        TournamentBet tournamentBet = tournamentBetMapper.toEntity(tournamentBetDTO);

        assertNull(tournamentBet);
    }

    @Test
    public void testUpdateTournamentBetFromDTONull() {
        TournamentBetUpdateDTO updateDTO = null;
        TournamentBet tournamentBet = TournamentBetUtil.tournamentBet();

        tournamentBetMapper.updateTournamentBetFromDTO(updateDTO, tournamentBet);

        assertNotNull(tournamentBet);
    }
}