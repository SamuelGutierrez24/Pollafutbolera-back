package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.UserSubpollaDTO;
import co.edu.icesi.pollafutbolera.mapper.UserSubpollaMapper;
import co.edu.icesi.pollafutbolera.model.UserSubPolla;
import co.edu.icesi.pollafutbolera.repository.UserSubPollaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSubpollaServiceImpl implements UserSubPollaService {

    private final UserSubPollaRepository userSubPollaRepository;
    private final UserSubpollaMapper mapper;


    @Override
    public ResponseEntity<List<UserSubpollaDTO>> getUsersBySubpollaId(Long subpollaId) {
        List<UserSubpollaDTO> dtos = userSubPollaRepository.findBySubpolla_Id(subpollaId).stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}
