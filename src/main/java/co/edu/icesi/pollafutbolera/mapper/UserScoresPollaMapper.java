package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.UserScoresPollaDTO;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserScoresPollaMapper {

    UserScoresPollaDTO toDTO(UserScoresPolla usp);

    UserScoresPolla toEntity(UserScoresPollaDTO dto);

}
