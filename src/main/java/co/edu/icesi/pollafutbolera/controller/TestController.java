package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.TestAPI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController implements TestAPI {

    @Override
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }

}