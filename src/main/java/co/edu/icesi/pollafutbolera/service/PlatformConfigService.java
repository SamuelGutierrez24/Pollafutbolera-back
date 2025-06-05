package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface PlatformConfigService {

    ResponseEntity<PlatformConfigDTO> getPlatformConfigById(Long id);

    ResponseEntity<List<PlatformConfigDTO>> getAllPlatformConfigs();

}
