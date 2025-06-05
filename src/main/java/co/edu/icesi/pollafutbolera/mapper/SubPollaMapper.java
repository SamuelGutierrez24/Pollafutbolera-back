package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.SubPolla;
import co.edu.icesi.pollafutbolera.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubPollaMapper {

    @Mapping(target = "pollaId", source = "polla.id")
    @Mapping(target = "userId", source = "user.id")
    SubPollaGetDTO toDTO(SubPolla subPolla);

    @Mapping(target = "pollaId", source = "polla.id")
    @Mapping(target = "userId", source = "user.id")
    List<SubPollaGetDTO> toDTOs(List<SubPolla> subPollas);

    @Mapping(target = "polla", source = "pollaId")
    @Mapping(target = "user", expression = "java(mapSubPollas(subPollaCreateDTO.userId()))")
    SubPolla toEntity(SubPollaCreateDTO subPollaCreateDTO);

    default Polla map(Long pollaId) {
        if (pollaId == null) {
            return null;
        }
        Polla polla = new Polla();
        polla.setId(pollaId);
        return polla;
    }

    default User mapSubPollas(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

}