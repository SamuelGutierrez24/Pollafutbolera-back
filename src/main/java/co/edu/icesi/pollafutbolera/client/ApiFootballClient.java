package co.edu.icesi.pollafutbolera.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiFootballClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${football.api.key}")
    private String apiKey;

    @Value("${football.api.url}")
    private String baseUrl;

    public String getStandings(Long league, Long season) {
        String url = buildUrl("standings", league, season);
        return sendRequest(url);
    }

    public String getTopScorers(Long league, Long season) {
        String url = buildUrl("players/topscorers", league, season);
        return sendRequest(url);
    }

    private String buildUrl(String path, Long league, Long season) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl + path)
                .queryParam("league", league)
                .queryParam("season", season)
                .toUriString();
    }

    private String sendRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Error al consumir API externa: " + url);
        }

        return response.getBody();
    }

    public ResponseEntity<String> getFixtureStatistics(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl+"/fixtures/statistics")
                .queryParam("fixture", id)
                .toUriString();

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
