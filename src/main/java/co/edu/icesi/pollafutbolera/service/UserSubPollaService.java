package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.UserSubpollaDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserSubPollaService {
    ResponseEntity<List<UserSubpollaDTO>> getUsersBySubpollaId(Long subpollaId);
}
