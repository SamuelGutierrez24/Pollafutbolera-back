package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class UserScoresPollaIntegrationTest {

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
    public void testUpdateUserScoresByPolla() {
        Long pollaId = 13L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/update-scores/" + pollaId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Boolean.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testUpdateUserScoresByMatch() {
        Long matchId = 14L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/update-scores/match/" + matchId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Boolean.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testgetRankingPolla() {
        Long pollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/polla/" + pollaId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        // Assuming the response body contains a JSON array of pollas with their IDs
        // Parse the response and check the order
        String responseBody = response.getBody();
        List<Long> pollaIds = parsePollaIdsFromResponse(responseBody); // Implement this method to parse IDs
        //14,23,16,17,15,18,20,21,22,19
        List<Long> expectedOrder = List.of(14L, 23L, 16L, 17L, 15L, 18L, 20L, 21L, 22L, 19L);
        assertEquals(expectedOrder, pollaIds); // Check that the first ID is 23
    }

    private List<Long> parsePollaIdsFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            List<Long> pollaIds = new ArrayList<>();
            for (JsonNode node : rootNode) {
                JsonNode userNode = node.get("user");
                if (userNode != null) {
                    pollaIds.add(userNode.get("id").asLong());
                }
            }
            return pollaIds;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response body", e);
        }
    }

    @Test
    public void testGetRankingSubPolla(){
        Long subPollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/polla/" + subPollaId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        String responseBody = response.getBody();
        List<Long> pollaIds = parsePollaIdsFromResponse(responseBody); // Implement this method to parse IDs
        //14,23,16,17,15,18,20,21,22,19
        List<Long> expectedOrder = List.of(14L, 23L, 16L, 17L, 15L, 18L, 20L, 21L, 22L, 19L);
        assertEquals(expectedOrder, pollaIds); // Check that the first ID is 23
    }

    @Test
    public void findRankingByUserIdAndPollaId(){
        Long userId = 14L;
        Long pollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/position/polla/" + pollaId + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<Long> entity = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Long.class
        );
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        Long ranking = response.getBody();
        assertEquals(1L, ranking);
    }

    @Test
    public void findRankingSubPolla(){
        Long userId = 14L;
        Long subPollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/userscorespolla/position/subpolla/" + subPollaId + "/users/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Long.class
        );
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        Long ranking = response.getBody();
        assertEquals(1L, ranking);
    }




}