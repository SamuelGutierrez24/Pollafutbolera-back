package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.SubPollaJoinRequestGetDTO;
import co.edu.icesi.pollafutbolera.model.SubPollaJoinRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubPollaJoinRequestMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "subpolla.id", target = "subpollaId")
    SubPollaJoinRequestGetDTO toDTO(SubPollaJoinRequest entity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "subpollaId", target = "subpolla.id")
    SubPollaJoinRequest toEntity(SubPollaJoinRequestGetDTO dto);
}
