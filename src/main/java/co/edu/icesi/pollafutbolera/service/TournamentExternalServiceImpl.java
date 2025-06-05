package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Tournament;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import co.edu.icesi.pollafutbolera.client.FootballApiClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentExternalServiceImpl implements TournamentExternalService {

    private final FootballApiClient apiFootballWebClient;
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;

    @Override
    public Mono<ResponseEntity<List<TournamentExternalDTO.LeagueData>>> getAllExternalLeagues() {
        return apiFootballWebClient.getAllLeagues()
                .flatMapIterable(TournamentExternalDTO::getResponse)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<TournamentExternalDTO>> getExternalLeagueById(Long leagueId, int season) {
        return apiFootballWebClient.getLeagueByIdExternal(leagueId, season)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<TournamentDTO>> saveExternalLeague(Long leagueId,int season) {
        return apiFootballWebClient.getLeagueByIdExternal(leagueId, season)
                .flatMap(response -> {
                    if (response.getResponse().isEmpty()) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    Tournament tournament = tournamentMapper.fromApiFootballLeague(response.getResponse().get(0));

                    return tournamentService.createTournamentReactive(tournamentMapper.toDTO(tournament))
                            .<ResponseEntity<TournamentDTO>>map(savedTournament -> ResponseEntity.ok(savedTournament))
                            .onErrorResume(e -> {
                                System.err.println("Error al guardar torneo: " + e.getMessage());
                                return Mono.just(ResponseEntity.<TournamentDTO>internalServerError().build());
                            });
                });
    }
}