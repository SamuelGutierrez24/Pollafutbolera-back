package co.edu.icesi.pollafutbolera.unit.config;

import co.edu.icesi.pollafutbolera.config.JwtAuthFilter;
import co.edu.icesi.pollafutbolera.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SecurityConfigurationTest {

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpSecurity httpSecurity;

    @InjectMocks
    private SecurityConfiguration securityConfiguration;

    @Test
    void corsConfigurationSource_ShouldConfigureCorsCorrectly() {

        MockHttpServletRequest request = new MockHttpServletRequest(); // Create a mock request

        // Act
        var corsConfigurationSource = securityConfiguration.corsConfigurationSource();
        var corsConfiguration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(corsConfiguration);
        assertTrue(corsConfiguration.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(corsConfiguration.getAllowedMethods().containsAll(
                java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")));
        assertTrue(corsConfiguration.getAllowedHeaders().contains("*"));
        assertTrue(corsConfiguration.getAllowCredentials());
    }
}
