package co.edu.icesi.pollafutbolera.api;


import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import com.twilio.http.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SubPolla API", description = "API for managing sub-pollas")
@RequestMapping("/sub-polla")
public interface SubPollaAPI {

    @GetMapping
    ResponseEntity<List<SubPollaGetDTO>> findAll();

    @GetMapping("/polla/{id}")
    ResponseEntity<List<SubPollaGetDTO>> findByPollaId(@PathVariable Long id);

    @GetMapping("/{id}")
    ResponseEntity<SubPollaGetDTO> findById(@PathVariable Long id);

    @PostMapping("/save")
    ResponseEntity<SubPollaGetDTO> save(@RequestBody SubPollaCreateDTO subPollaCreateDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSubPolla(@PathVariable Long id);

    @GetMapping("/creator/{creatorUserId}")
    ResponseEntity<List<SubPollaGetDTO>> findByCreatorUserId(@PathVariable Long creatorUserId);

}