package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.exception.BetNotAvailableException;
import co.edu.icesi.pollafutbolera.exception.UniqueBetException;
import co.edu.icesi.pollafutbolera.mapper.MatchBetMapper;
import co.edu.icesi.pollafutbolera.model.MatchBet;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.repository.MatchBetRepository;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransientPropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchBetServiceImpl implements MatchBetService {
    private final MatchBetRepository matchBetRepository;
    private final MatchBetMapper matchBetMapper;
    private final MatchRepository matchRepository;

    @Override
    public ResponseEntity<MatchBetResponseDTO> getMatchBetById(Long id) {

        MatchBet matchBet = matchBetRepository.findMatchBetById(id);

        if (matchBet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(matchBetMapper.toResponseDTO(matchBet));
    }

    @Override
    public ResponseEntity<MatchBetResponseDTO> createMatchBet(MatchBetDTO matchBetDTO) {
        try {
            MatchBet matchBet = matchBetMapper.toEntity(matchBetDTO);
            boolean exists = matchBetRepository.existsByUserAndMatchAndPolla(
                    matchBet.getUser().getId(), matchBet.getMatch().getId(), matchBet.getPolla().getId());
            if (exists) {
                throw new UniqueBetException();
            }

            Long matchId = matchBet.getMatch().getId();
            Match match = matchRepository.findById(matchId).orElse(null);
            if (match == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            LocalDateTime matchDate = match.getDate();
            LocalDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
            if (now.isBefore(matchDate.minusHours(24)) || now.isAfter(matchDate.minusMinutes(30))) {
                throw new BetNotAvailableException();
            }

            matchBet = matchBetRepository.save(matchBet);
            MatchBetResponseDTO savedMatchBetResponseDTO = matchBetMapper.toResponseDTO(matchBet);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMatchBetResponseDTO);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUser(Long userId) {
        List<MatchBet> matchBets = matchBetRepository.findMatchBetsByUser(userId);
        List<MatchBetResponseDTO> matchBetResponseDTOs = matchBets.stream()
                .map(matchBetMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchBetResponseDTOs);
    }

    @Override
    public ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByUserAndPolla(Long userId, Long pollaId) {
        List<MatchBet> matchBets = matchBetRepository.findMatchBetsByUserAndPolla(userId, pollaId);
        List<MatchBetResponseDTO> matchBetResponseDTOs = matchBets.stream()
                .map(matchBetMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchBetResponseDTOs);
    }

    @Override
    public ResponseEntity<MatchBetResponseDTO> updateMatchBet(Long id, MatchBetUpdateDTO updateMatchBetDTO) {

        try {
            MatchBet existingMatchBet = matchBetRepository.findById(id).orElse(null);
            if (existingMatchBet == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Long matchId = existingMatchBet.getMatch().getId();
            Match match = matchRepository.findById(matchId).orElse(null);
            if (match == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            LocalDateTime matchDate = match.getDate();
            LocalDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
            if (now.isBefore(matchDate.minusHours(24)) || now.isAfter(matchDate.minusMinutes(30))) {
                throw new BetNotAvailableException();
            }

            matchBetMapper.updateMatchBetFromDTO(updateMatchBetDTO, existingMatchBet);
            MatchBet updatedMatchBet = matchBetRepository.save(existingMatchBet);
            MatchBetResponseDTO updatedMatchBetResponseDTO = matchBetMapper.toResponseDTO(updatedMatchBet);
            return ResponseEntity.ok(updatedMatchBetResponseDTO);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<Void> deleteMatchBet(Long id) {
        if (!matchBetRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        matchBetRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<MatchBetResponseDTO>> getMatchBetsByMatch(Long matchId) {
        List<MatchBet> matchBets = matchBetRepository.findByMatch(matchId);

        if (matchBets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<MatchBetResponseDTO> matchBetResponseDTOs = matchBets.stream()
                .map(matchBetMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(matchBetResponseDTOs);
    }
}
