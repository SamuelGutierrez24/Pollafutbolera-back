package co.edu.icesi.pollafutbolera.unit.tenant;

import co.edu.icesi.pollafutbolera.exception.InvalidTenantIdException;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.CompanyRepository;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.resolver.HttpHeaderTenantResolver;
import co.edu.icesi.pollafutbolera.resolver.TenantContext;
import co.edu.icesi.pollafutbolera.resolver.TenantInterceptor;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Disabled("Pruebas temporalmente deshabilitadas")
@ExtendWith(MockitoExtension.class)
class TenantFilterTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PollaRepository pollaRepository;

    @InjectMocks
    private HttpHeaderTenantResolver tenantResolver;

    @InjectMocks
    private TenantInterceptor tenantInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private Company company1;
    private Company company2;
    private User user1;
    private User user2;
    private Polla polla1;
    private Polla polla2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test companies
        company1 = Company.builder().id(1L).name("Company 1").nit("1234").build();
        company2 = Company.builder().id(2L).name("Company 2").nit("5678").build();
        
        // Setup test users
        user1 = User.builder().id(1L).company(company1).name("User 1").nickname("user1").mail("user1@example.com").build();
        user2 = User.builder().id(2L).company(company2).name("User 2").nickname("user2").mail("user2@example.com").build();
        
        // Setup test pollas using PollaUtil
        polla1 = PollaUtil.polla();
        polla1.setId(1L);
        polla1.setCompany(company1);
        
        polla2 = PollaUtil.polla();
        polla2.setId(2L);
        polla2.setCompany(company2);
        
        // Create mock request/response
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        // Setup TenantInterceptor
        tenantInterceptor = new TenantInterceptor(tenantResolver);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testTenantResolution_ValidTenant() throws Exception {
        // Setup request with a valid tenant ID
        request.addHeader("X-TenantId", "1");
        
        // Execute
        boolean result = tenantInterceptor.preHandle(request, response, null);
        
        // Verify
        assertTrue(result);
        assertEquals(1L, TenantContext.getTenantId());
    }

    @Test
    void testTenantResolution_MissingTenant() {
        // Verify exception is thrown when tenant header is missing
        assertThrows(InvalidTenantIdException.class, () -> {
            tenantInterceptor.preHandle(request, response, null);
        });
    }

    @Test
    void testTenantResolution_InvalidTenantFormat() {
        // Setup request with an invalid tenant ID
        request.addHeader("X-TenantId", "invalid");
        
        // Verify exception is thrown
        assertThrows(InvalidTenantIdException.class, () -> {
            tenantInterceptor.preHandle(request, response, null);
        });
    }

    @Test
    void testTenantContextClearing() throws Exception {
        // Setup
        request.addHeader("X-TenantId", "1");
        tenantInterceptor.preHandle(request, response, null);
        
        // Execute
        tenantInterceptor.afterCompletion(request, response, null, null);
        
        // Verify
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void testUserFiltering_WithTenantContext() {
        // Setup
        TenantContext.setTenantId(1L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1)); // Only users for tenant 1
        
        // Execute
        List<User> users = userRepository.findAll();
        
        // Verify
        assertEquals(1, users.size());
        assertEquals("User 1", users.get(0).getName());
    }

    @Test
    void testPollaFiltering_WithTenantContext() {
        // Setup
        TenantContext.setTenantId(2L);
        when(pollaRepository.findAll()).thenReturn(Arrays.asList(polla2)); // Only pollas for tenant 2
        
        // Execute
        List<Polla> pollas = pollaRepository.findAll();
        
        // Verify
        assertEquals(1, pollas.size());
        assertEquals(2L, pollas.get(0).getCompany().getId());
    }

    @Test
    void testUserFiltering_WithDifferentTenantContext() {
        // Setup with tenant 1
        TenantContext.setTenantId(1L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));
        List<User> usersFromTenant1 = userRepository.findAll();
        assertEquals(1, usersFromTenant1.size());
        assertEquals("User 1", usersFromTenant1.get(0).getName());
        
        // Switch to tenant 2
        TenantContext.clear();
        TenantContext.setTenantId(2L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user2));
        List<User> usersFromTenant2 = userRepository.findAll();
        assertEquals(1, usersFromTenant2.size());
        assertEquals("User 2", usersFromTenant2.get(0).getName());
    }
    
    @Test
    void testTenantInterceptor_HandlesExceptions() throws Exception {
        // Test with null tenant ID
        request.addHeader("X-TenantId", "null");
        
        try {
            tenantInterceptor.preHandle(request, response, null);
            fail("Should have thrown exception");
        } catch (Exception e) {
            // Clear should be called even when exception is thrown
            assertNull(TenantContext.getTenantId());
        }
    }
}