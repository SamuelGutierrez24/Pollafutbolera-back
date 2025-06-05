package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.MatchBetDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetUpdateDTO;
import co.edu.icesi.pollafutbolera.dto.MatchBetResponseDTO;
import co.edu.icesi.pollafutbolera.model.MatchBet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatchBetMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "match.id", target = "matchId")
    @Mapping(source = "polla.id", target = "pollaId")
    MatchBetDTO toDTO(MatchBet matchBet);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(source = "pollaId", target = "polla.id")
    MatchBet toEntity(MatchBetDTO matchBetDTO);

    @Mapping(target = "id", ignore = true)
    void updateMatchBetFromDTO(MatchBetUpdateDTO updateMatchBetDTO, @MappingTarget MatchBet matchBet);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "match", target = "match")
    @Mapping(source = "polla.id", target = "pollaId")
    MatchBetResponseDTO toResponseDTO(MatchBet matchBet);
}
