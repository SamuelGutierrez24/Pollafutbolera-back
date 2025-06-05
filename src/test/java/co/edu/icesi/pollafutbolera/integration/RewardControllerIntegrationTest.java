package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.RewardDTO;
import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.RewardSaveDTO;
import co.edu.icesi.pollafutbolera.util.RewardUtil;
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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class RewardControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetRewardByPolla() {
        Long pollaId = 1L;
        String url = "http://localhost:" + port + "/pollafutbolera/reward/polla/" + pollaId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<RewardDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                RewardDTO[].class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    public void testSaveRewards() {
        String url = "http://localhost:" + port + "/pollafutbolera/reward/save";
        RewardSaveDTO[] rewardDTOs = RewardUtil.rewardSaveDTOs().toArray(new RewardSaveDTO[0]);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<RewardSaveDTO[]> entity = new HttpEntity<>(rewardDTOs, headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(200, response.getStatusCode().value());
    }
    //TODO: uncomment this test when the update method is implemented
    /*
    @Test
    public void testUpdateReward() {
        Long rewardId = 1L;
        String url = "http://localhost:" + port + "/pollafutbolera/reward/" + rewardId;
        RewardDTO rewardDTO = RewardUtil.rewardDTOs().get(0);

        HttpEntity<RewardDTO> requestEntity = new HttpEntity<>(rewardDTO);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        assertEquals(200, response.getStatusCode().value());
    }*/
}