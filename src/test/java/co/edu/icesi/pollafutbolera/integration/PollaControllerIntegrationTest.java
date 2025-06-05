package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class PollaControllerIntegrationTest {

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
    public void testFindAll() {
        String url = "http://localhost:" + port + "/pollafutbolera/polla";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<PollaGetDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PollaGetDTO[].class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPolla() throws Exception {
        Long pollaId = 11L;
        String url = "http://localhost:" + port + "/pollafutbolera/polla/" + pollaId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<PollaGetDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PollaGetDTO.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(pollaId, response.getBody().id());
    }

    @Test
    public void testSavePolla() {
        String url = "http://localhost:" + port + "/pollafutbolera/polla/save";
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<PollaConfigDTO> entity = new HttpEntity<>(pollaConfigDTO, headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testUpdatePolla() {
        Long pollaId = 12L;
        String url = "http://localhost:" + port + "/pollafutbolera/polla/" + pollaId;
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<PollaConfigDTO> entity = new HttpEntity<>(pollaConfigDTO, headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        System.out.println("Response: " + response);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testDeletePolla() {
        Long pollaId =10L;
        String url = "http://localhost:" + port + "/pollafutbolera/polla/" + pollaId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1"); // Añadir tu token o cualquier otro encabezado

        // Crear HttpEntity con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET con los headers
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(200, response.getStatusCode().value());
    }
}