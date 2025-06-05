package co.edu.icesi.pollafutbolera.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/fixture")
@CrossOrigin(origins = "http://localhost:3000")
public interface FixtureAPI {

    @GetMapping("/stats/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<?> getStatidistics(@PathVariable Long id);

}