package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.Tournament;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PlatformConfigMapper.class, TournamentMapper.class, RewardMapper.class})
public interface PollaMapper {

    @Mapping(target = "tournamentId", source = "tournament.id")
    @Mapping(target = "platformConfig", source = "platformConfig")
    @Mapping(target = "company", source = "company.id")
    PollaConfigDTO toPollaConfigDTO(Polla polla);

    @Mapping(target = "tournament", source = "tournamentId")
    @Mapping(target = "platformConfig", source = "platformConfig")
    @Mapping(target = "company", expression = "java(mapCompany(pollaConfigDTO.company()))")
    Polla toPolla(PollaConfigDTO pollaConfigDTO);

    @Mapping(target = "tournament", source = "tournament")
    @Mapping(target = "platformConfig", source = "platformConfig")
    PollaGetDTO toPollaGetDTO(Polla polla);

    default Tournament map(Long tournamentId) {
        if (tournamentId == null) {
            return null;
        }
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        return tournament;
    }


    default Company mapCompany(Long companyId) {
        if (companyId == null) {
            return null;
        }
        Company company = new Company();
        company.setId(companyId);
        return company;
    }




}