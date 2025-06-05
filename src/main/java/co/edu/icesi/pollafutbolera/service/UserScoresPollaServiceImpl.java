package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.dto.UserScoresPollaDTO;
import co.edu.icesi.pollafutbolera.enums.PollaFutboleraExceptionType;
import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.enums.UserPollaState;
import co.edu.icesi.pollafutbolera.exception.MatchNotFoundException;
import co.edu.icesi.pollafutbolera.exception.PollaFutboleraException;
import co.edu.icesi.pollafutbolera.exception.PollaNotFoundException;
import co.edu.icesi.pollafutbolera.exception.UserNotInPollaException;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.mapper.UserMapper;
import co.edu.icesi.pollafutbolera.mapper.UserScoresPollaMapper;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import co.edu.icesi.pollafutbolera.exception.TournamentStatisticsNotFoundException;
import org.hibernate.metamodel.model.domain.PluralPersistentAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserScoresPollaServiceImpl implements UserScoresPollaService {

    private final UserScoresPollaRepository userScoresPollaRepository;

    private final UserMapper userMapper;
    private final PollaMapper pollaMapper;

    private final UserScoresPollaMapper userScoresPollaMapper;
    private final MatchRepository matchRepository;
    private final MatchBetRepository matchBetRepository;
    private final UserRepository userRepository;
    private final PollaRepository pollaRepository;
    private final PlatformConfigRepository platformConfigRepository;
    private final JwtService jwtService;

    private final TournamentBetService tournamentBetService;

    @Autowired
    private final ScoresEnd pollaService;
    private final SubPollaRepository subPollaRepository;
    private final TournamentStatisticsRepository tournamentStatisticsRepository;


    @Override
    public ResponseEntity<List<UserDTO>> getUsersByPollaId(Long pollaId) throws PollaNotFoundException {


        List<UserScoresPolla> userScoresPollas = userScoresPollaRepository.findByPollaId(pollaId);

        if(userScoresPollas.isEmpty()){
            throw new PollaNotFoundException("Polla not found with ID: " + pollaId);
        }

        List<User> users = userScoresPollas.stream()
                .map(UserScoresPolla::getUser).toList();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        List<UserDTO> userDTOS = users.stream()
                .map(userMapper::toDTO)
                .toList();

        return ResponseEntity.ok(userDTOS);
    }

    @Override
    public ResponseEntity<UserScoresPollaDTO> findByUserIdAndPollaId(Long userId, Long pollaId) throws UserNotInPollaException {
        UserScoresPolla userScoresPolla = userScoresPollaRepository.findByUser_IdAndPolla_Id(userId, pollaId)
                .orElseThrow(UserNotInPollaException::new);

        return ResponseEntity.ok(userScoresPollaMapper.toDTO(userScoresPolla));

    }

    @Override
    public ResponseEntity<Boolean> banUserFromPolla(Long userId, Long pollaId) {
        try {
            Optional<UserScoresPolla> optionalUserScoresPolla = userScoresPollaRepository.findByUser_IdAndPolla_Id(userId, pollaId);

            if (optionalUserScoresPolla.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }

            UserScoresPolla userScoresPolla = optionalUserScoresPolla.get();

            userScoresPolla.setState(UserPollaState.BLOQUEADO);
            userScoresPollaRepository.save(userScoresPolla);

            return ResponseEntity.ok(true);
        } catch (UserNotInPollaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

    }

    @Override
    public ResponseEntity<Boolean> updateUserScoresByPolla(Long pollaId) {
        List<TournamentBet> bets = tournamentBetService.getTournamentBetsByPolla(pollaId);

        if (bets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        Tournament tournament = bets.get(0).getTournament();

        if (tournament.getWinner_team_id() == null || tournament.getTop_scoring_team_id() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        PlatformConfigDTO plataformConfig = pollaService.findPollaById(pollaId).getBody().platformConfig();

        int tournamentChampionPoints = plataformConfig.tournamentChampion();
        int teamWithMostGoalsPoints = plataformConfig.teamWithMostGoals();

        for (TournamentBet bet : bets) {
            long userId = bet.getUser().getId();
            int earnedPoints = 0;

            TournamentStatistics tournamentStatistics = tournamentStatisticsRepository
                    .findById(tournament.getId())
                    .orElseThrow(() -> new TournamentStatisticsNotFoundException("No se encontraron estadísticas para el torneo con ID: " + tournament.getName()));

            Long winnerTeamId= tournamentStatistics.getWinnerTeamId();

            if ( winnerTeamId.equals(bet.getWinnerTeam().getId())) {
                earnedPoints += tournamentChampionPoints;
            }

            if (winnerTeamId.equals(bet.getTopScoringTeam().getId())) {
                earnedPoints += teamWithMostGoalsPoints;
            }

            tournamentBetService.updateEarnedPointsByUserAndPolla(userId, pollaId, earnedPoints);

            UserScoresPolla userScoresPolla = userScoresPollaRepository.findByUser_IdAndPolla_Id(userId, pollaId)
                    .orElseThrow(UserNotInPollaException::new);

            userScoresPolla.setScores(userScoresPolla.getScores() + earnedPoints);
            userScoresPollaRepository.save(userScoresPolla);
        }
        return ResponseEntity.ok(true);
    }

    @Transactional
    @Override
    public ResponseEntity<Boolean> updateUserScoresByMatch(Long matchId) {

        List<MatchBet> matchBets = matchBetRepository.findByMatch(matchId);

        if (matchBets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        Match match = matchBets.get(0).getMatch();

        Long pollaId = 0L;

        int exactScorePoints = 0;
        int matchWinnerPoints = 0;

        for (MatchBet matchBet : matchBets) {

            Long newPollaId = matchBet.getPolla().getId();

            if(!pollaId.equals(newPollaId)){
                pollaId= newPollaId;

                PlatformConfigDTO plataformConfig = pollaService.findPollaById(pollaId).getBody().platformConfig();

                exactScorePoints = plataformConfig.exactScore();
                matchWinnerPoints = plataformConfig.matchWinner();
            }

            int earnedPoints = 0;

            if (matchBet.getHomeScore().equals(match.getHomeScore()) &&
                    matchBet.getAwayScore().equals(match.getAwayScore())) {
                earnedPoints += exactScorePoints;
            }

            if(matchBet.getHomeScore() > matchBet.getAwayScore() &&
                    match.getHomeScore() > match.getAwayScore()) {

                earnedPoints += matchWinnerPoints;

            }else if (matchBet.getHomeScore() < matchBet.getAwayScore() &&
                    match.getHomeScore() < match.getAwayScore()) {

                earnedPoints += matchWinnerPoints;

            } else if  (matchBet.getHomeScore().equals(matchBet.getAwayScore()) &&
                    match.getHomeScore().equals(match.getAwayScore())) {

                earnedPoints += matchWinnerPoints;
            }

            matchBet.setEarnedPoints(earnedPoints);
            matchBetRepository.save(matchBet);

            UserScoresPolla userScoresPolla = userScoresPollaRepository.findByUser_IdAndPolla_Id(
                            matchBet.getUser().getId(), matchBet.getPolla().getId())
                    .orElseThrow(UserNotInPollaException::new);

            userScoresPolla.setScores(userScoresPolla.getScores() + earnedPoints);
            userScoresPollaRepository.save(userScoresPolla);
        }
        return ResponseEntity.ok(true);
    }

    @Override
    public ResponseEntity<List<Long>> getPollaIdsByUserId(Long userId) {
        List<Long> pollaIds = userScoresPollaRepository.findPollaIdsByUserId(userId);
        if (pollaIds == null || pollaIds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pollaIds);
    }

    @Transactional
    @Override
    public ResponseEntity<List<PollaGetDTO>> getPollasByUserId(String token) {
        Long userId = jwtService.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.badRequest().build(); // Return 400 if the user ID is invalid
        }

        List<Polla> pollas = userScoresPollaRepository.findPollaByUserId(userId);
        return ResponseEntity.ok(pollas.stream()
                .map(pollaMapper::toPollaGetDTO)
                .collect(Collectors.toList()));
    }


    @Override
    public ResponseEntity<Boolean> createRelation(Long userId, Long pollaId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new PollaFutboleraException(PollaFutboleraExceptionType.USER_NOT_FOUND));
            Polla polla = pollaRepository.findById(pollaId)
                    .orElseThrow(() -> new PollaFutboleraException(PollaFutboleraExceptionType.POLLA_NOT_FOUND));

            UserScoresPolla userScoresPolla = new UserScoresPolla();
            userScoresPolla.setUser(user);
            userScoresPolla.setPolla(polla);
            userScoresPolla.setState(UserPollaState.ACTIVO);
            userScoresPolla.setScores(0);

            userScoresPollaRepository.save(userScoresPolla);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    public ResponseEntity<List<RankingDTO>> rankingBySubPolla(Long subPollaId, Pageable pageable) {
        try{
            List<UserScoresPolla> userScoresPollas = userScoresPollaRepository.rankingBySubPolla(subPollaId, pageable);

            List<RankingDTO> rankingDTOS = userScoresPollas.stream()
                    .map(userScoresPolla -> {
                        UserDTO userDTO = userMapper.toDTO(userScoresPolla.getUser());
                        return new RankingDTO(userDTO, userScoresPollaMapper.toDTO(userScoresPolla));
                    })
                    .toList();

            return ResponseEntity.ok(rankingDTOS);
        }
        catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Long> findRankingByUserIdAndPollaId(Long userId, Long pollaId) throws UserNotInPollaException {
        try{
            pollaRepository.findById(pollaId);
            Optional<Long> ranking = userScoresPollaRepository.findRankingByUserIdAndPollaId(userId, pollaId);
            if (ranking.isPresent()) {
                return ResponseEntity.ok(ranking.get());
            } else {
                throw new UserNotInPollaException();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (UserNotInPollaException e) {
            throw e;
        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Long> findRankingSubPolla(Long userId, Long subPollaId) throws UserNotInPollaException {
        try{
            subPollaRepository.findById(subPollaId);
            System.err.println("SubPollaId: " + subPollaId);
            Optional<Long> ranking = userScoresPollaRepository.findRankingSubPolla(userId, subPollaId);
            if (ranking.isPresent()) {
                return ResponseEntity.ok(ranking.get());
            } else {
                throw new UserNotInPollaException();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (UserNotInPollaException e) {
            throw e;
        }
        catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<RankingDTO>> rankingByPolla(Long pollaId, Pageable pageable){
        try{
            pollaRepository.findById(pollaId);

            List<UserScoresPolla> userScoresPollas = userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(pollaId, pageable);

            List<RankingDTO> rankingDTOS = userScoresPollas.stream()
                    .map(userScoresPolla -> {
                        UserDTO userDTO = userMapper.toDTO(userScoresPolla.getUser());
                        UserScoresPollaDTO userScoresPollaDTO = userScoresPollaMapper.toDTO(userScoresPolla);
                        return new RankingDTO(userDTO, userScoresPollaDTO);
                    })
                    .toList();
            return ResponseEntity.ok(rankingDTOS);
        }
        catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().build();
        }

    }


}
