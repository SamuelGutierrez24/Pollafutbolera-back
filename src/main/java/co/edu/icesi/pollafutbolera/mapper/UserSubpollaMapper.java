package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.UserSubpollaDTO;
import co.edu.icesi.pollafutbolera.model.UserSubPolla;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSubpollaMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "subpolla.id", target = "subpollaId")
    UserSubpollaDTO toDTO(UserSubPolla entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "subpolla.id", source = "subpollaId")
    UserSubPolla toEntity(UserSubpollaDTO dto);
}
