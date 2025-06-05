package co.edu.icesi.pollafutbolera.unit.service;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import co.edu.icesi.pollafutbolera.config.JwtAuthFilter;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.JwtService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;
    
    private User testUser;
    private UserDetails userDetails;
    
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
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

        userDetails = org.springframework.security.core.userdetails.User.builder()
            .username("testuser")
            .password("encodedPassword")
            .build();
    }
    
    @Test
    void doFilterInternal_ShouldSkipFilter_WhenPathIsAuthenticate() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/user/authenticate");
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }
    
    @Test
    void doFilterInternal_ShouldReturn401_WhenAuthHeaderIsNull() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(jwtService);
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    void doFilterInternal_ShouldReturn401_WhenAuthHeaderDoesNotStartWithBearer() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Invalid token");
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(jwtService);
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    void doFilterInternal_ShouldDoNothing_WhenUsernameIsNull() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn(null);
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    void doFilterInternal_ShouldDoNothing_WhenAuthenticationExists() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testuser");
        
        Authentication existingAuth = new UsernamePasswordAuthenticationToken("testuser", null);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    void doFilterInternal_ShouldContinueFilter_WhenUserNotFound() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.empty());
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(userRepository, times(1)).findByNickname("testuser");
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    void doFilterInternal_ShouldDoNothing_WhenTokenIsInvalid() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid("validToken", testUser)).thenReturn(false);
        
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(userRepository, times(1)).findByNickname("testuser");
        verify(jwtService, times(1)).isTokenValid("validToken", testUser);
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenTokenIsValid() throws ServletException, IOException, java.io.IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid("validToken", testUser)).thenReturn(true);

        WebAuthenticationDetailsSource detailsSource = mock(WebAuthenticationDetailsSource.class);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(userRepository, times(1)).findByNickname("testuser");
        verify(jwtService, times(1)).isTokenValid("validToken", testUser);
        verify(filterChain, times(1)).doFilter(request, response);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testuser", authentication.getName());
    }
}