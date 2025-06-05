package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.util.MatchUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class MatchControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                String body = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
                System.err.println("Response error: " + body);
                super.handleError(response);
            }
        });
    }

    @Test
    public void testFindAllMatches() {
        String url = "http://localhost:" + port + "/pollafutbolera/api/matches";

        ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(url, MatchDTO[].class);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetMatchById() {
        Long matchId = 12L;
        String url = "http://localhost:" + port + "/pollafutbolera/api/matches/" + matchId;

        ResponseEntity<MatchDTO> response = restTemplate.getForEntity(url, MatchDTO.class);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(matchId, response.getBody().id());
    }

    @Test
    public void testCreateMatch() {
        String url = "http://localhost:" + port + "/pollafutbolera/api/matches";
        MatchDTO matchDTO = MatchUtil.matchDTO();

        ResponseEntity<Void> response = restTemplate.postForEntity(url, matchDTO, Void.class);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    public void testUpdateMatch() {
        Long matchId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/api/matches/" + matchId;
        MatchDTO matchDTO = MatchUtil.matchDTO2();

        HttpEntity<MatchDTO> requestEntity = new HttpEntity<>(matchDTO);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testDeleteMatch() {
        Long matchId = 15L;
        String url = "http://localhost:" + port + "/pollafutbolera/api/matches/" + matchId;

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(204, response.getStatusCode().value());
    }
}