package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.service.FootballService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
public class FootballController {

    private final FootballService footballService;

    @PostMapping("/fixtures/save")
    public ResponseEntity<List<MatchDTO>> fetchAndSaveFixtures(@RequestParam int league, @RequestParam int season, @RequestParam String round) {
        System.out.println(league + season + round);
        return footballService.fetchAndSaveFixtures(league, season, round);
    }
    @PostMapping("/fixtures/save/all")
    public ResponseEntity<List<MatchDTO>> saveAllFixtures(@RequestParam int league, @RequestParam int season) {
        return footballService.saveAllFixtures(league, season);
    }

}