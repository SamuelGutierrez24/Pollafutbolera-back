package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.util.SubPollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class SubPollaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // Prevent exceptions for non-2xx status codes
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // Log the error response for debugging purposes
                String body = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
                System.err.println("Response error: " + body);
            }
        });
    }

    @Test
    public void
    testFindByPollaId() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/polla/11";

        ResponseEntity<SubPollaGetDTO[]> response = restTemplate.getForEntity(url, SubPollaGetDTO[].class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    public void testFindById() {
        Long subPollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/" + subPollaId;

        ResponseEntity<SubPollaGetDTO> response = restTemplate.getForEntity(url, SubPollaGetDTO.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(subPollaId, response.getBody().id());
    }

    @Test
    public void testSave() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/save";
        SubPollaCreateDTO subPollaCreateDTO = SubPollaUtil.createSampleSubPollaCreateDTOs().get(0);

        ResponseEntity<PollaResponseEntity> response = restTemplate.postForEntity(url, subPollaCreateDTO, PollaResponseEntity.class);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    /*
    Flaky test
    @Test
    public void testFindAll() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla";

        ResponseEntity<SubPollaGetDTO[]> response = restTemplate.getForEntity(url, SubPollaGetDTO[].class);
        assertNotNull(response);
        System.err.println("Response: " + Arrays.toString(response.getBody()));
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);
    }
    */


    @Test
    public void testFindByPollaIdInvalidId() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/polla/999";

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testFindByIdInvalidId() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/999";

        ResponseEntity<SubPollaGetDTO> response = restTemplate.getForEntity(url, SubPollaGetDTO.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testSaveInvalidData() {
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/save";
        SubPollaCreateDTO invalidDTO = new SubPollaCreateDTO(true, null, null); // Missing required fields

        ResponseEntity<String> response = restTemplate.postForEntity(url, invalidDTO, String.class);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void testDeleteSubPolla() {
        Long subPollaId = 10L;
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/" + subPollaId;

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    public void testDeleteSubPollaInvalidId() {
        Long subPollaId = 999L;
        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla/" + subPollaId;

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(404, response.getStatusCode().value());
    }
    /*
    @Test
    public void testFindAllEmptyDatabase() {

        String url = "http://localhost:" + port + "/pollafutbolera/sub-polla";

        ResponseEntity<SubPollaGetDTO[]> response = restTemplate.getForEntity(url, SubPollaGetDTO[].class);
        assertEquals(204, response.getStatusCode().value());
    }
    */

}