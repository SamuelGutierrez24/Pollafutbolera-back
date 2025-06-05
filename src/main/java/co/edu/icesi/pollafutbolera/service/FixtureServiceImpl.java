package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.client.ApiFootballClient;
import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import co.edu.icesi.pollafutbolera.mapper.MatchStatsMapper;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.MatchStats;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.MatchStatsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FixtureServiceImpl implements FixtureService{
    
    @Autowired
    private MatchStatsRepository matchStatsRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchStatsMapper matchStatsMapper;

    @Autowired
    private ApiFootballClient apiFootballClient;

    @Override
    public List<FixtureDTO> fixtureStatics(Long id) throws Exception {

        Optional<Match> match = matchRepository.findById(id);
        if(match.isEmpty()){
            throw new Exception("Match not found");
        }

        List<MatchStats> matchStats = matchStatsRepository.findByMatchId(match.get());

        if(matchStats.isEmpty()){
            ResponseEntity<String> response = apiFootballClient.getFixtureStatistics(id);
            ObjectMapper objectMapper = new ObjectMapper();
            List<FixtureDTO> fixtures = objectMapper.readValue(objectMapper.readTree(response.getBody()).get("response").toString(),
                    new TypeReference<List<FixtureDTO>>() {});

            for(FixtureDTO fixtureDTO :fixtures){
                MatchStats stats = matchStatsMapper.completeMatchStats(fixtureDTO);
                stats.setMatchId(matchRepository.findById(id).get());
                matchStatsRepository.save(stats);
            }

            return fixtures;
        }
        List<FixtureDTO> fixtures = new ArrayList<>();
        for(MatchStats statsTeams: matchStats){
           fixtures.add(matchStatsMapper.completeFixtureDTO(statsTeams));
        }

        return fixtures;

    }
}
