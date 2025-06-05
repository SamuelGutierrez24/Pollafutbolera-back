package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.exception.BetNotAvailableException;
import co.edu.icesi.pollafutbolera.exception.UniqueBetException;
import co.edu.icesi.pollafutbolera.mapper.TournamentBetMapper;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import co.edu.icesi.pollafutbolera.repository.TournamentBetRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentBetServiceImpl implements TournamentBetService {
    private final TournamentBetRepository tournamentBetRepository;
    private final TournamentBetMapper tournamentBetMapper;
    private final TournamentRepository tournamentRepository;

    @Override
    public ResponseEntity<TournamentBetDTO> getTournamentBetById(Long id) {
        TournamentBet tournamentBet = tournamentBetRepository.findTournamentBetById(id);

        if (tournamentBet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TournamentBetDTO tournamentBetDTO = tournamentBetMapper.toDTO(tournamentBet);
        return ResponseEntity.ok(tournamentBetDTO);
    }

    @Override
    public ResponseEntity<TournamentBetDTO> createTournamentBet(TournamentBetDTO tournamentBetDTO) {
        try {
            TournamentBet tournamentBet = tournamentBetMapper.toEntity(tournamentBetDTO);

            boolean exists = tournamentBetRepository.existsByUserAndPolla(
                    tournamentBet.getUser().getId(), tournamentBet.getPolla().getId());

            if (exists) {
                throw new UniqueBetException();
            }

            Long tournamentId = tournamentBet.getTournament().getId();
            var tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(IllegalArgumentException::new);

            LocalDateTime tournamentStartDate = tournament.getInitial_date().atStartOfDay();
            LocalDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
            if (now.isAfter(tournamentStartDate)) {
                throw new BetNotAvailableException();
            }



            tournamentBet = tournamentBetRepository.save(tournamentBet);
            TournamentBetDTO savedTournamentBetDTO = tournamentBetMapper.toDTO(tournamentBet);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentBetDTO);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<TournamentBetDTO> updateTournamentBet(Long id, TournamentBetUpdateDTO updateTournamentBetDTO) {
        try {
            TournamentBet existingTournamentBet = tournamentBetRepository.findById(id).orElse(null);
            if (existingTournamentBet == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Long tournamentId = existingTournamentBet.getTournament().getId();
            var tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(IllegalArgumentException::new);

            LocalDateTime tournamentStartDate = tournament.getInitial_date().atStartOfDay();
            LocalDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
            if (now.isAfter(tournamentStartDate)) {
                throw new BetNotAvailableException();
            }

            TournamentBetDTO tournamentBetDTO = tournamentBetMapper.toDTO(existingTournamentBet);

            if (updateTournamentBetDTO.winnerTeamId() != null) {
                tournamentBetDTO = tournamentBetDTO.toBuilder().winnerTeamId(updateTournamentBetDTO.winnerTeamId()).build();
            }
            if (updateTournamentBetDTO.topScoringTeamId() != null) {
                tournamentBetDTO = tournamentBetDTO.toBuilder().topScoringTeamId(updateTournamentBetDTO.topScoringTeamId()).build();
            }

            TournamentBet updatedTournamentBet = tournamentBetMapper.toEntity(tournamentBetDTO);

            updatedTournamentBet = tournamentBetRepository.save(updatedTournamentBet);
            TournamentBetDTO updatedTournamentBetDTO = tournamentBetMapper.toDTO(updatedTournamentBet);
            return ResponseEntity.ok(updatedTournamentBetDTO);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<Void> deleteTournamentBet(Long id) {
        if (!tournamentBetRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tournamentBetRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<TournamentBetDTO> getTournamentBetByUserAndPolla(Long userId, Long pollaId) {
        TournamentBet tournamentBet = tournamentBetRepository.findTournamentBetByUserAndPolla(userId, pollaId);

        if (tournamentBet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TournamentBetDTO tournamentBetDTO = tournamentBetMapper.toDTO(tournamentBet);
        return ResponseEntity.ok(tournamentBetDTO);
    }

    @Override
    public List<TournamentBet> getTournamentBetsByPolla(Long pollaId) {
        return  tournamentBetRepository.findByPollaId(pollaId);
    }
    @Override
    public void updateEarnedPointsByUserAndPolla(Long userId, Long pollaId, int earnedPoints) {
        TournamentBet tournamentBet = tournamentBetRepository.findTournamentBetByUserAndPolla(userId, pollaId);

        if (tournamentBet == null) {
            throw new IllegalArgumentException("TournamentBet not found" );
        }

        tournamentBet.setEarnedPoints(earnedPoints);
        tournamentBetRepository.save(tournamentBet);
    }
}
