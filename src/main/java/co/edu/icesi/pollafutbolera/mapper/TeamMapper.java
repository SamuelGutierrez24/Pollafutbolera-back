package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.dto.TeamDTO;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import co.edu.icesi.pollafutbolera.repository.TeamRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TeamMapper {

    @Autowired
    private TeamRepository teamRepository;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "logoUrl", target = "logoUrl")
    public abstract TeamDTO toTeamDTO(Team entity);

    @Mapping(target = "id", ignore = true)
    public abstract Team toTeam(TeamDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "logoUrl", target = "logoUrl")
    public abstract List<TeamDTO> listToTeamDTO(List<Team> entities);

    @Mapping(target = "id", ignore = true)
    public abstract List<Team> listToTeam(List<TeamDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDTO(TeamDTO dto, @MappingTarget Team entity);
}
