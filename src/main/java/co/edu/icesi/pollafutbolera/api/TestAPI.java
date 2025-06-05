package co.edu.icesi.pollafutbolera.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("")
public interface TestAPI {

    @GetMapping
    ResponseEntity<String> test();

}
