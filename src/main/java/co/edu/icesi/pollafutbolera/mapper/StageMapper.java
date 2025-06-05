package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.dto.StageDTO;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StageMapper {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private StageRepository stageRepository;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "stageName")
    @Mapping(source = "tournament.id", target = "tournamentId")
    public abstract StageDTO toStageDTO(Stage entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "stageName")
    @Mapping(target = "tournament", expression ="java(mapTournament(dto.tournamentId()))")
    public abstract Stage toStage(StageDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "stageName")
    @Mapping(source = "tournament.ID", target = "tournamentId")
    public abstract List<StageDTO> listToStageDTO(List<Stage> entities);

    @Mapping(target = "id", ignore = true)
    public abstract List<Stage> listToStage(List<StageDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDTO(StageDTO dto, @MappingTarget Stage entity);


    @Named("fromJson")
    public StageDTO fromJson(JsonNode json, Long tournamentId) {
        String name = json.asText();


        return StageDTO.builder()
                .stageName(name)
                .deletedAt(null)
                .tournamentId(tournamentId)
                .build();
    }
    protected Tournament mapTournament(Long tournamentId) {
        if (tournamentId == null) return null;
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));
    }
}