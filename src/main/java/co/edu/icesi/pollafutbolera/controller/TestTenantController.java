package co.edu.icesi.pollafutbolera.controller;


import co.edu.icesi.pollafutbolera.resolver.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TestTenantController {
    @GetMapping
    Long getTenant() {

        return TenantContext.getTenantId();
    }
}
