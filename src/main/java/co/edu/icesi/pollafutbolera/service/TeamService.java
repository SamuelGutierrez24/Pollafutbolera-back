package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import co.edu.icesi.pollafutbolera.model.Team;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamService extends GenericService<TeamDTO, Long> {
    ResponseEntity<TeamDTO> createTeam(TeamDTO teamDTO);
    ResponseEntity<Void> fetchAndSaveTeams(Integer league, Integer season);
    ResponseEntity<List<TeamDTO>> fetchTeamsByLeagueAndSeason(Integer league, Integer season);
    ResponseEntity<List<TeamDTO>> getTeamsInTournament(Long tournamentId);
}
