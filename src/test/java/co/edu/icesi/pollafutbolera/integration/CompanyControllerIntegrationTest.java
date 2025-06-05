package co.edu.icesi.pollafutbolera.integration;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.util.CompanyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.profiles.active=test",
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"})
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class CompanyControllerIntegrationTest {

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
    public void testCreateCompany() {
        String url = "http://localhost:" + port + "/pollafutbolera/company/save";
        CreateCompanyDTO createCompanyDTO = CompanyUtil.createCompanyDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "777");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear HttpEntity con los headers
        HttpEntity<CreateCompanyDTO> entity = new HttpEntity<>(createCompanyDTO, headers);

        ResponseEntity<CompanyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CompanyDTO.class
        );

        assertEquals(HttpResponseStatus.CREATED.code(), response.getStatusCode().value());
    }

    @Test
    public void testGetCompanyById() {
        Long companyId = 10L;
        String url = "http://localhost:" + port + "/pollafutbolera/company/id/" + companyId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "777");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CompanyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CompanyDTO.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().id());
    }


    @Test
    public void testGetCompanyByName() {
        String companyName = "Default Company";
        String url = "http://localhost:" + port + "/pollafutbolera/company/name/" + companyName;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "777");

        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<CompanyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CompanyDTO.class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(companyName, response.getBody().name());
    }

    @Test
    public void testUpdateCompanyById(){
        Long companyId = 12L;
        String url = "http://localhost:" + port + "/pollafutbolera/company/" + companyId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-TenantId", "1");

        UpdateCompanyDTO updateCompanyDTO = new UpdateCompanyDTO(
                "Nueva Empresa",
                null,
                "emp@gmail.com"
        );

        HttpEntity<UpdateCompanyDTO> entity = new HttpEntity<>(updateCompanyDTO, headers);

        ResponseEntity<CompanyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                entity,
                CompanyDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().id());
        assertEquals("Nueva Empresa", response.getBody().name());
        assertEquals("emp@gmail.com", response.getBody().contact());
        assertEquals("address 3", response.getBody().address());
    }

    @Test
    public void testGetAllCompanies(){
        String url = "http://localhost:" + port + "/pollafutbolera/company/companies";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "777");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CompanyDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CompanyDTO[].class
        );

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().length);
    }

    @Test
    public void testDeleteCompanyById(){
        Long companyId = 13L;
        String url = "http://localhost:" + port + "/pollafutbolera/company/" + companyId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantId", "1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CompanyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                CompanyDTO.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().id());



    }
}
