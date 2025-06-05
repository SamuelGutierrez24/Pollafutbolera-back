package co.edu.icesi.pollafutbolera.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.icesi.pollafutbolera.exception.BlankFieldException;
import co.edu.icesi.pollafutbolera.exception.IncorrectPasswordException;
import co.edu.icesi.pollafutbolera.exception.UsernameNotFoundException;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AppConfig {

    @Autowired
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
       return username -> {
            final User user = userRepository.findByNickname(username)
                    .orElseThrow(UsernameNotFoundException::new);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getNickname())
                    .password(user.getPassword())
                    .build();
       };

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(){
            @Override
            protected void additionalAuthenticationChecks(
                                                    UserDetails userDetails, 
                                                    UsernamePasswordAuthenticationToken authentication) 
                    throws AuthenticationException {
                if (authentication.getCredentials() == null) {
                    throw new BlankFieldException();
                }
                
                String presentedPassword = authentication.getCredentials().toString();
                
                if (!passwordEncoder().matches(presentedPassword, userDetails.getPassword())) {
                    throw new IncorrectPasswordException(); 
                }
            }
        };
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
