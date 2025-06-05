package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Polla API", description = "API for managing pollas")
@RequestMapping("/polla")
public interface PollaAPI {

    @Operation(summary = "Get all pollas, returns an array of PollaGetDTO")
    @GetMapping
    ResponseEntity<PollaGetDTO[]> getPolla();

    @Operation(summary = "Get a polla by ID, returns a PollaGetDTO")
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("permitAll()")
    ResponseEntity<PollaGetDTO> getPolla(@PathVariable Long id);

    @Operation(summary = "Save a new polla, requires a PollaConfigDTO, a PollaConfigDTO has a start date, an end date, a boolean stating weather or not its private, a link to an image, a json containing the points to be given for each prediction, and the Id of the tournament that it uses, returns void")
    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    ResponseEntity<PollaGetDTO> savePolla(@RequestBody PollaConfigDTO polla);

    @Operation(summary = "Update an existing polla, requires an ID and a PollaConfigDTO, a PollaConfigDTO has a start date, an end date, a boolean stating weather or not its private, a link to an image, a json containing the points to be given for each prediction, and the Id of the tournament that it uses, returns void")
    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<PollaGetDTO> updatePolla(@PathVariable Long id, @RequestBody PollaConfigDTO polla);

    @Operation(summary = "Delete a polla by ID, returns void")
    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    void deletePolla(@PathVariable Long id);

    @Operation(summary = "Finish a polla by ID, returns a list of UserScoresPolla")
    @GetMapping("/finish/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<List<UserScoresPolla>> finishPolla(@PathVariable Long id);

    @Operation(summary = "Get all pollas by company ID, returns an array of PollaGetDTO")
    @GetMapping("/company/{id}")
    @PreAuthorize("permitAll()")
    ResponseEntity<PollaGetDTO[]> findPollasByCompanyId(@PathVariable Long id);
}