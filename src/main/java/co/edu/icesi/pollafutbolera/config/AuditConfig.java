package co.edu.icesi.pollafutbolera.config;

import co.edu.icesi.pollafutbolera.resolver.TenantContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null && authentication.isAuthenticated() ? 
                            authentication.getName() : "system";
            
            Long tenantId = TenantContext.getTenantId();
            if (tenantId != null) {
                return Optional.of("tenant-" + tenantId + ":" + username);
            }
            
            return Optional.of(username);
        };
    }
}