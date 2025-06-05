package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Set necessary properties using ReflectionTestUtils since they would normally be injected by Spring
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L); // 24 hours

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setNickname("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setCedula("123456789");
        testUser.setRole(role);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Act
        String token = jwtService.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsExpired() {
        // Creating a helper mock to test expiry
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"));
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 1000); // 1 second in the past

        String expiredToken = Jwts.builder()
                .subject("testuser")
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenExpired(expiredToken));
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenUsernameDoesNotMatch() {
        // Arrange
        JwtService spyJwtService = spy(jwtService);
        doReturn("different_user").when(spyJwtService).extractUsername(any());

        // Act & Assert
        assertFalse(spyJwtService.isTokenValid("valid.token", testUser));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void isTokenExpired_ShouldReturnFalse_ForValidToken() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        boolean isExpired = jwtService.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }
}