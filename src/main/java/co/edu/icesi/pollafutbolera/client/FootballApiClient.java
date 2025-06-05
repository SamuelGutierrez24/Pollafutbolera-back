package co.edu.icesi.pollafutbolera.client;

import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Json;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Getter
@Service
public class FootballApiClient {

    private final WebClient webClient;

    public FootballApiClient(
            @Value("${football.api.key:placeholder}") String apiKey,
            @Value("${football.api.url:placeholder}") String baseUrl,
            @Value("${football.api.header:placeholder}") String header,
            WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(header, apiKey)
                .build();
    }


    public Mono<JsonNode> getFixtures(Integer league, Integer season, String round) {

        // Construir la URI con los parámetros dinámicos
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParamIfPresent("league", Optional.ofNullable(league))
                        .queryParamIfPresent("season", Optional.ofNullable(season))
                        .queryParamIfPresent("round", Optional.ofNullable(round))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);  // Devolver directamente el JsonNode
        
    }
    public Mono<JsonNode> getAllFixtures(Integer league, Integer season) {

        // Construir la URI con los parámetros dinámicos
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParamIfPresent("league", Optional.ofNullable(league))
                        .queryParamIfPresent("season", Optional.ofNullable(season))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);  // Devolver directamente el JsonNode

    }

    public Mono<JsonNode> getMatch(Integer id){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures")
                        .queryParamIfPresent("id", Optional.ofNullable(id))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    // Grupos

    public Mono<JsonNode> getStandings(Integer league, Integer season) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/standings")
                        .queryParamIfPresent("league", Optional.ofNullable(league))
                        .queryParamIfPresent("season", Optional.ofNullable(season))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    //Equipos
    public Mono<JsonNode> getTeams(Integer league, Integer season) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/teams")
                        .queryParamIfPresent("league", Optional.ofNullable(league))
                        .queryParamIfPresent("season", Optional.ofNullable(season))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
    // Round

    public Mono<JsonNode> getStagesOfTournament(Long league, Integer season) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures/rounds")
                        .queryParamIfPresent("league", Optional.ofNullable(league))
                        .queryParamIfPresent("season", Optional.ofNullable(season))
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    public Mono<JsonNode> getLeagueById(Long leagueId, int season) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/leagues")
                        .queryParam("id", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    // Torneos Externos
    public Mono<TournamentExternalDTO> getLeagueByIdExternal(Long leagueId, int season) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/leagues")
                        .queryParam("id", leagueId)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(TournamentExternalDTO.class);
    }

    public Mono<TournamentExternalDTO> getAllLeagues() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/leagues")
                        .queryParam("season", 2023)
                        .queryParam("current", "true")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.err.println("Error de API: " + errorBody);
                                return Mono.error(new RuntimeException("Error en API: " + errorBody));
                            });
                })
                .bodyToMono(TournamentExternalDTO.class)
                .doOnNext(response -> System.out.println("Datos recibidos: " + response.getResponse().size()));
    }
}