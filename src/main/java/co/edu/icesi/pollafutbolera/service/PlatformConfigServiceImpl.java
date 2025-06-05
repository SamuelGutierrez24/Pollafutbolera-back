package co.edu.icesi.pollafutbolera.service;


import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.mapper.PlatformConfigMapper;
import co.edu.icesi.pollafutbolera.model.PlatformConfig;
import co.edu.icesi.pollafutbolera.repository.PlatformConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformConfigServiceImpl implements PlatformConfigService{
    private final PlatformConfigRepository platformConfigRepository;
    private final PlatformConfigMapper platformConfigMapper;

    @Override
    public ResponseEntity<PlatformConfigDTO> getPlatformConfigById(Long id) {
        PlatformConfig platformConfig = platformConfigRepository.findById(id).orElse(null);
        PlatformConfigDTO platformConfigDTO = platformConfigMapper.toDTO(platformConfig);
        return ResponseEntity.ok(platformConfigDTO);
    }

    @Override
    public ResponseEntity<List<PlatformConfigDTO>> getAllPlatformConfigs() {
        List<PlatformConfig> platformConfig = platformConfigRepository.findAll();
        List<PlatformConfigDTO> platformConfigDTOs = platformConfigMapper.toDTOList(platformConfig);
        System.out.println("\n\n\n"+platformConfigDTOs);
        return ResponseEntity.ok(platformConfigDTOs);
    }
}