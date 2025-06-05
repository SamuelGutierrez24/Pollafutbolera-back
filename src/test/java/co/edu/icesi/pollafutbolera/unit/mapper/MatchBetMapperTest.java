package co.edu.icesi.pollafutbolera.unit.mapper;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.mapper.MatchBetMapper;
import co.edu.icesi.pollafutbolera.model.MatchBet;
import co.edu.icesi.pollafutbolera.util.MatchBetUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class MatchBetMapperTest {

    MatchBetMapper matchBetMapper = Mappers.getMapper(MatchBetMapper.class);

    @Test
    public void testToMatchBetDTO() {
        MatchBet matchBet = MatchBetUtil.matchBet();

        MatchBetDTO matchBetDTO = matchBetMapper.toDTO(matchBet);

        assertNotNull(matchBetDTO);
        assertEquals(matchBet.getId(), matchBetDTO.id());
        assertEquals(matchBet.getHomeScore(), matchBetDTO.homeScore());
        assertEquals(matchBet.getAwayScore(), matchBetDTO.awayScore());
        assertEquals(matchBet.getEarnedPoints(), matchBetDTO.earnedPoints());
        assertEquals(matchBet.getUser().getId(), matchBetDTO.userId());
        assertEquals(matchBet.getMatch().getId(), matchBetDTO.matchId());
        assertEquals(matchBet.getPolla().getId(), matchBetDTO.pollaId());
    }

    @Test
    public void testToMatchBet() {
        MatchBetDTO matchBetDTO = MatchBetUtil.matchBetDTO();

        MatchBet matchBet = matchBetMapper.toEntity(matchBetDTO);

        assertNotNull(matchBet);
        assertEquals(matchBetDTO.id(), matchBet.getId());
        assertEquals(matchBetDTO.homeScore(), matchBet.getHomeScore());
        assertEquals(matchBetDTO.awayScore(), matchBet.getAwayScore());
        assertEquals(matchBetDTO.earnedPoints(), matchBet.getEarnedPoints());
        assertEquals(matchBetDTO.userId(), matchBet.getUser().getId());
        assertEquals(matchBetDTO.matchId(), matchBet.getMatch().getId());
        assertEquals(matchBetDTO.pollaId(), matchBet.getPolla().getId());
    }

    @Test
    public void testUpdateMatchBetFromDTO() {
        MatchBetUpdateDTO updateDTO = MatchBetUpdateDTO.builder()
                .homeScore(3)
                .awayScore(2)
                .build();
        MatchBet matchBet = MatchBetUtil.matchBet();

        matchBetMapper.updateMatchBetFromDTO(updateDTO, matchBet);

        assertEquals(updateDTO.homeScore(), matchBet.getHomeScore());
        assertEquals(updateDTO.awayScore(), matchBet.getAwayScore());
    }

    @Test
    public void testToMatchBetDTONull() {
        MatchBet matchBet = null;

        MatchBetDTO matchBetDTO = matchBetMapper.toDTO(matchBet);

        assertNull(matchBetDTO);
    }

    @Test
    public void testToMatchBetNull() {
        MatchBetDTO matchBetDTO = null;

        MatchBet matchBet = matchBetMapper.toEntity(matchBetDTO);

        assertNull(matchBet);
    }

    @Test
    public void testUpdateMatchBetFromDTONull() {
        MatchBetUpdateDTO updateDTO = null;
        MatchBet matchBet = MatchBetUtil.matchBet();

        matchBetMapper.updateMatchBetFromDTO(updateDTO, matchBet);

        assertNotNull(matchBet);
    }

    @Test
    public void testToMatchBetResponseDTO() {
        MatchBet matchBet = MatchBetUtil.matchBet();

        MatchBetResponseDTO matchBetResponseDTO = matchBetMapper.toResponseDTO(matchBet);

        assertNotNull(matchBetResponseDTO);
        assertEquals(matchBet.getId(), matchBetResponseDTO.id());
        assertEquals(matchBet.getHomeScore(), matchBetResponseDTO.homeScore());
        assertEquals(matchBet.getAwayScore(), matchBetResponseDTO.awayScore());
        assertEquals(matchBet.getEarnedPoints(), matchBetResponseDTO.earnedPoints());
        assertEquals(matchBet.getUser().getId(), matchBetResponseDTO.userId());
        assertEquals(matchBet.getMatch().getId(), matchBetResponseDTO.match().id());
        assertEquals(matchBet.getPolla().getId(), matchBetResponseDTO.pollaId());
    }
}