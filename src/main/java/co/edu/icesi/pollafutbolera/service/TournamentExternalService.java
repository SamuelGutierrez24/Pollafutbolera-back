package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TournamentExternalService {
    Mono<ResponseEntity<List<TournamentExternalDTO.LeagueData>>> getAllExternalLeagues();
    Mono<ResponseEntity<TournamentExternalDTO>> getExternalLeagueById(Long leagueId,int season);
    Mono<ResponseEntity<TournamentDTO>> saveExternalLeague(Long leagueId, int season);
}