package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface FixtureService {

    public List<FixtureDTO> fixtureStatics(Long id) throws Exception;
}
