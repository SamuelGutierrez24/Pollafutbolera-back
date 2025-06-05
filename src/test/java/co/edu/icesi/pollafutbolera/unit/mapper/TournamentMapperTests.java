package co.edu.icesi.pollafutbolera.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.service.TournamentExternalService;

@SpringBootTest
public class TournamentMapperTests {

    @Mock
    TournamentExternalService tournamentExternalService;
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    @Test
    void mapTest_DtoToTournament_ShouldReturnEntity() {
        TournamentDTO tournamentDTO = new TournamentDTO(1l, "Liga 1", "Colombia", null, null, 2L, 3L, 4L, null);

        Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
    
        assertEquals(1L, tournament.getId());
        assertEquals("Liga 1", tournament.getName());
        assertEquals("Colombia", tournament.getDescription());
    }

    @Test
    void mapTest_TournamentToDTO_ShouldReturnDTO() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Liga 1");
        tournament.setDescription("Colombia");
        tournament.setWinner_team_id(2L);
        tournament.setFewest_goals_conceded_team_id(3L);
        tournament.setTop_scoring_team_id(4L);
        tournament.setDeleted_at(null);
    
        TournamentDTO tournamentDTO = tournamentMapper.toDTO(tournament);
    
        assertEquals(1L, tournamentDTO.getId());
        assertEquals("Liga 1", tournamentDTO.getName());
        assertEquals("Colombia", tournamentDTO.getDescription());

    }

    @Test
    void mapTest_FromAPIDTOToEntity_ShouldReturnEntity() {
        TournamentExternalDTO.LeagueData.League league = new TournamentExternalDTO.LeagueData.League();
        league.setId(1L);
        league.setName("Premier League");
        league.setType("League");
        league.setLogo("https://example.com/logo.png");

        TournamentExternalDTO.LeagueData.Country country = new TournamentExternalDTO.LeagueData.Country();
        country.setName("England");
        country.setCode("ENG");
        country.setFlag("https://example.com/flag.png");

        TournamentExternalDTO.LeagueData leagueData = new TournamentExternalDTO.LeagueData();
        leagueData.setLeague(league);
        leagueData.setCountry(country);

        Tournament tournament = tournamentMapper.fromApiFootballLeague(leagueData);

        assertNotNull(tournament);
        assertEquals(1L, tournament.getId());
        assertEquals("Premier League", tournament.getName());
        assertEquals("Torneo de England", tournament.getDescription());
    }
    
}
