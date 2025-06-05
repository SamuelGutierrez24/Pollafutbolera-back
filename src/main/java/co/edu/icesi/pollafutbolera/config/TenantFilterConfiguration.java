package co.edu.icesi.pollafutbolera.config;

import co.edu.icesi.pollafutbolera.resolver.TenantContext;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class TenantFilterConfiguration implements HandlerInterceptor {

    private final EntityManagerFactory entityManagerFactory;

    public TenantFilterConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {

        /*
        //ignore api/match and /sub-polla requests and /login
        if (request.getRequestURI().contains("/api/match") || request.getRequestURI().contains("/sub-polla") || request.getRequestURI().contains("user/authenticate")) {
            return true;
        }

        if (RequestContextHolder.getRequestAttributes() != null && TenantContext.getTenantId() != null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter").setParameter("tenantId", TenantContext.getTenantId());
            entityManager.close();
        }

         */
        return true;
    }
}