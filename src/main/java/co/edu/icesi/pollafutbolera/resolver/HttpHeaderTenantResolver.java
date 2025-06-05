package co.edu.icesi.pollafutbolera.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

    @Override
    public Long resolveTenantId(HttpServletRequest request) {
        return Long.valueOf(request.getHeader("X-TenantId"));
    }
}