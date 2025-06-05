package co.edu.icesi.pollafutbolera.api;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/platformconfig")
public interface PlatformConfigAPI {

    @GetMapping("/{id}")
    ResponseEntity<PlatformConfigDTO> getPlatformConfigById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<PlatformConfigDTO>> getAllPlatformConfigs();

}
