package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.PermissionDTO;
import co.edu.icesi.pollafutbolera.model.Permission;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    PermissionDTO toDTO(Permission permission);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Permission toEntity(PermissionDTO permissionDTO);

    List<PermissionDTO> toDTOList(List<Permission> permissions);
    List<Permission> toEntityList(List<PermissionDTO> permissionDTOs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(PermissionDTO permissionDTO, @MappingTarget Permission permission);
}