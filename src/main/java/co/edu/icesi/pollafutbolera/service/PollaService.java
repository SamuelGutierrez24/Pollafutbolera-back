package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PollaService {

    ResponseEntity<PollaGetDTO> savePolla(PollaConfigDTO polla);
    void deletePolla(Long id);
    ResponseEntity<PollaGetDTO> updatePolla(Long id, PollaConfigDTO polla);
    ResponseEntity<PollaGetDTO[]> findAllPollas();
    ResponseEntity<PollaGetDTO> findPollaById(Long id);
    void findPollaByName(String name);
    ResponseEntity<PollaGetDTO[]> findPollasByCompanyId(Long id);
    public ResponseEntity<List<UserScoresPolla>> endPolla(Long id);

}
