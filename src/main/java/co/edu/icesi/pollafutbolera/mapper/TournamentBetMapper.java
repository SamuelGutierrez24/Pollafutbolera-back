package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.TournamentBetDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentBetUpdateDTO;
import co.edu.icesi.pollafutbolera.model.TournamentBet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentBetMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "polla.id", target = "pollaId")
    @Mapping(source = "winnerTeam.id", target = "winnerTeamId")
    @Mapping(source = "topScoringTeam.id", target = "topScoringTeamId")
    TournamentBetDTO toDTO(TournamentBet tournamentBet);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tournamentId", target = "tournament.id")
    @Mapping(source = "pollaId", target = "polla.id")
    @Mapping(source = "winnerTeamId", target = "winnerTeam.id")
    @Mapping(source = "topScoringTeamId", target = "topScoringTeam.id")
    TournamentBet toEntity(TournamentBetDTO tournamentBetDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "winnerTeamId", target = "winnerTeam.id")
    @Mapping(source = "topScoringTeamId", target = "topScoringTeam.id")
    void updateTournamentBetFromDTO(TournamentBetUpdateDTO updateTournamentBetDTO, @MappingTarget TournamentBet tournamentBet);
}