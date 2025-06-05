package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.NotificationDTO;
import co.edu.icesi.pollafutbolera.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mappings({
        @Mapping(source = "user.id", target = "userId"),
        @Mapping(source = "type.id", target = "typeId"),
        @Mapping(source = "type.name", target = "typeName")
    })
    NotificationDTO toDTO(Notification notification);
}
