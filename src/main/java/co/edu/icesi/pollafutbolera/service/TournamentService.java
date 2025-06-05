package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.http.ResponseEntity;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import reactor.core.publisher.Mono;

public interface TournamentService {

    ResponseEntity<List<TournamentDTO>> getAllTournaments();
    String getStandings(Long league, Long season);
    ResponseEntity<TournamentStatsDTO> updateTournamentStats(Long tournamentId);
    ResponseEntity<TournamentStatistics> getTournamentStatistics(Long tournamentId);
    ResponseEntity<Tournament> getTournament(Long tournamentId);
    ResponseEntity<TournamentDTO> getTournamentById(Long id);
    ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO);
    ResponseEntity<TournamentDTO> updateTournament(Long id, TournamentDTO tournamentDTO);
    ResponseEntity<Void> deleteTournament(Long id);
    Mono<TournamentDTO> createTournamentReactive(TournamentDTO tournamentDTO);
    //ResponseEntity<TournamentDTO> register(Long leagueId, int season);
    ResponseEntity<List<TournamentDTO>>getLeagueById(Long leagueId, int season);
    ResponseEntity<List<TournamentDTO>> getActiveTournamentsByDate(LocalDate date);
}

