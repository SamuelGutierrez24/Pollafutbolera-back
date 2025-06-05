package co.edu.icesi.pollafutbolera.resolver;

import co.edu.icesi.pollafutbolera.exception.InvalidTenantIdException;
import co.edu.icesi.pollafutbolera.exception.TenantNotProvidedInRequestHeader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private final HttpHeaderTenantResolver tenantResolver;

    public TenantInterceptor(HttpHeaderTenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
        /*
        //ignore api/match and //sub-polla requests
        if (request.getRequestURI().contains("/api/match") || request.getRequestURI().contains("/sub-polla") || request.getRequestURI().contains("user/authenticate")|| request.getRequestURI().contains("/tournament/register")
                || request.getRequestURI().contains("/api/external/tournaments/") || request.getRequestURI().contains("user/preloadedusers/")) {
            return true;
        }

        try {
            Long tenantId = tenantResolver.resolveTenantId(request);
            if (tenantId != null) {
                TenantContext.setTenantId(tenantId);
            } else {
                throw new InvalidTenantIdException();
            }
        } catch (NumberFormatException e) {
            clear();
            throw new InvalidTenantIdException();
        } catch (Exception e) {
            clear();
            throw e;
        }
        return true;
         */
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        clear();
    }

    public void clear() {
        TenantContext.clear();
    }

}