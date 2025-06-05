package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.exception.MatchNotFoundException;
import co.edu.icesi.pollafutbolera.mapper.MatchMapper;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {


    private final MatchRepository repository;

    private final MatchMapper mapper;

    private final StageRepository stageRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    public ResponseEntity<List<MatchDTO>> findAll() {
        List<Match> match = repository.findAll();
        List<MatchDTO> matchDTO = mapper.listToMatchDTO(match);
        return ResponseEntity.ok(matchDTO);

    }

    @Override
    public ResponseEntity<MatchDTO> findById(Long id) throws Exception{

        try {
            Optional<Match> match = repository.findById(id);
            if(match.isPresent()){
                MatchDTO matchDTO = mapper.toMatchDTO(match.get());
                return ResponseEntity.ok(matchDTO);
            }else{
                return ResponseEntity.notFound().build();
            }

        }catch (Exception e){
            throw new Exception("La entidad no existe");
        }

    }


    @Override
    public ResponseEntity<MatchDTO> createMatch(MatchDTO matchDTO) {
        Match match = mapper.toMatch(matchDTO);
        match = repository.save(match);

        MatchDTO createdUserDTO = mapper.toMatchDTO(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                createdUserDTO
        );
    }

    @Override
    public ResponseEntity<MatchDTO> save(Long id, MatchDTO entity){

        Optional<Match> existingMatch = repository.findById(id);

        if (existingMatch.isPresent()) {

            Match match = existingMatch.get();

            mapper.updateFromDTO(entity, match);

            repository.save(match);

            MatchDTO matchDTO = mapper.toMatchDTO(match);
            return ResponseEntity.ok(matchDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @Override
    public ResponseEntity<List<MatchDTO>> findByStageAndTournament(Long id, Long tournament) throws Exception {
        try {
            Optional<Stage> stage = stageRepository.findById(id);
            if(stage.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Optional<Tournament> tournament1 = tournamentRepository.findById(tournament);
            if(tournament1.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Optional<List<Match>> match = repository.findByStageAndTournament(stage.get(), tournament1.get());
            if(match.isPresent()){
                List<MatchDTO> matchDTO = mapper.listToMatchDTO(match.get());
                return ResponseEntity.ok(matchDTO);
            }else{
                return ResponseEntity.notFound().build();
            }

        }catch (Exception e){
            throw new MatchNotFoundException();
        }
    }

    @Override
    public ResponseEntity<List<MatchDTO>> findByTournament(Long tournament) throws Exception {
        try{
            Optional<Tournament> tournament1 = tournamentRepository.findById(tournament);
            if(tournament1.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Optional<List<Match>> match = repository.findByTournament(tournament1.get());
            if(match.isPresent()){
                List<MatchDTO> matchDTO = mapper.listToMatchDTO(match.get());
                return ResponseEntity.ok(matchDTO);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            throw new MatchNotFoundException();
        }
    }

    @Override
    public ResponseEntity<MatchDTO> apiMatchSave(MatchDTO matchDTO) {
        Match match = mapper.toMatch(matchDTO);
        repository.save(match);
        return ResponseEntity.ok(matchDTO);
    }

    @Override
    public ResponseEntity<List<MatchDTO>>findByTournamentAndDateBetween(Long tournamentId, LocalDateTime start, LocalDateTime end) throws Exception {
        // look for matches by tournament and date
        try {
            Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
            if (tournament.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<Match> matches = repository.findByTournamentAndDateBetween(tournament.get(), start, end);
            if (matches.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<MatchDTO> matchDTOs = mapper.listToMatchDTO(matches);
            return ResponseEntity.ok(matchDTOs);
        } catch (Exception e) {
            throw new MatchNotFoundException();
        }
    }


    @Override
    public void deleteById(Long id) throws Exception{

        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        }else{
            throw new MatchNotFoundException();
        }

    }


}