package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.PlatformConfigDTO;
import co.edu.icesi.pollafutbolera.model.PlatformConfig;
import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlatformConfigMapper {

    @Mapping(source = "tournamentChampion", target = "tournamentChampion")
    @Mapping(source = "teamWithMostGoals", target = "teamWithMostGoals")
    @Mapping(source = "exactScore", target = "exactScore")
    @Mapping(source = "matchWinner", target = "matchWinner")
    PlatformConfigDTO toDTO(PlatformConfig platformConfig);

    List<PlatformConfigDTO> toDTOList(List<PlatformConfig> platformConfig);

}
