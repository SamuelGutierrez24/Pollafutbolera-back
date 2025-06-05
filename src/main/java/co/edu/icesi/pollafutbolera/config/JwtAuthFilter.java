package co.edu.icesi.pollafutbolera.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
@Profile("!test")
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    private final UserRepository userRepository;

   
    @Override
    public void doFilterInternal( @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {


        String path = request.getServletPath();



        if(request.getServletPath().contains("/user/authenticate") || request.getServletPath().contains("/email/reset-password") 
        		|| request.getServletPath().contains("/changePassword") || request.getServletPath().contains("/user/create")  
        		|| request.getServletPath().contains("/images/upload")  || request.getServletPath().contains("/nickname/exists") 
        		|| request.getServletPath().contains("/user/preloadedusers") || request.getServletPath().contains("/userscorespolla/create")) {

           filterChain.doFilter(request, response);
           return;
        }

       final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              return;
        }

        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            
            return;
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        final Optional<User> user = userRepository.findByNickname(userDetails.getUsername());

        if (user.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        final boolean isTokenValid = jwtService.isTokenValid(token, user.get());

        if(!isTokenValid) {
            
            return;
        }

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
             null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        filterChain.doFilter(request, response);
    }
}