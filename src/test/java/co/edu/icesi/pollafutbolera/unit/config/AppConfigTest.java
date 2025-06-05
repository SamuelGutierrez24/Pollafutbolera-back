package co.edu.icesi.pollafutbolera.unit.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.icesi.pollafutbolera.config.AppConfig;
import co.edu.icesi.pollafutbolera.exception.IncorrectPasswordException;
import co.edu.icesi.pollafutbolera.exception.UsernameNotFoundException;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AppConfig appConfig;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setNickname("testuser");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("correctPassword"); // Encode "correctPassword"

        testUser.setPassword(encodedPassword); // Store the encoded password
        testUser.setName("Test User");
        testUser.setCedula("123456789");
        testUser.setRole(role);
    }

    @Test
    void userDetailsService_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = appConfig.userDetailsService().loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("correctPassword", userDetails.getPassword()));
        verify(userRepository, times(1)).findByNickname("testuser");
    }

    @Test
    void userDetailsService_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByNickname("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
            appConfig.userDetailsService().loadUserByUsername("nonexistent"));
        verify(userRepository, times(1)).findByNickname("nonexistent");
    }

    
    @Test
    void authenticationProvider_ShouldThrowIncorrectPasswordException_WhenPasswordDoesNotMatch() {
        // Arrange
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));

        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken("testuser", "wrongPassword");
        
        // Act & Assert
        assertThrows(IncorrectPasswordException.class, () -> 
            appConfig.authenticationProvider().authenticate(authentication));
    }
    
    @Test
    void authenticationProvider_ShouldReturnAuthentication_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));

        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken("testuser", "correctPassword");
        
        // Act

        Authentication result = appConfig.authenticationProvider().authenticate(authentication);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals("testuser", result.getName());
    }
}
