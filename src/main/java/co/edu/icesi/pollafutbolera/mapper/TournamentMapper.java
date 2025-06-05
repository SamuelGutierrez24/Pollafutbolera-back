package co.edu.icesi.pollafutbolera.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import co.edu.icesi.pollafutbolera.dto.TournamentDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentExternalDTO;
import co.edu.icesi.pollafutbolera.dto.TournamentStatsDTO;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.model.TournamentStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class TournamentMapper {

    // ====== MapStruct mappings ======

    public abstract TournamentDTO toDTO(Tournament tournament);

    public abstract Tournament toEntity(TournamentDTO tournamentDTO);

    //Mapeo desde API-Football
    @Mapping(target = "id", source = "league.id")
    @Mapping(target = "name", source = "league.name")
    @Mapping(target = "description", expression = "java(\"Torneo de \" + apiFootballLeague.getCountry().getName())")
    @Mapping(target = "initial_date", expression = "java(getInitialDate(apiFootballLeague))")
    @Mapping(target = "final_date", expression = "java(getFinalDate(apiFootballLeague))")
    //Variables de la base de datos que no se pueden obtener desde la API Leagues
    @Mapping(target = "winner_team_id", ignore = true)
    @Mapping(target = "fewest_goals_conceded_team_id", ignore = true)
    @Mapping(target = "top_scoring_team_id", ignore = true)
    @Mapping(target = "deleted_at", ignore = true)
    public abstract Tournament fromApiFootballLeague(TournamentExternalDTO.LeagueData apiFootballLeague);

    // ====== Métodos personalizados ======

    public TournamentDTO fromJson(JsonNode leagueInfo, int season, JsonNode season1Info) {
        Long id = leagueInfo.get("id").asLong();
        String name = leagueInfo.get("name").asText();
        LocalDate startDate = LocalDate.parse(season1Info.get("start").asText());
        LocalDate endDate = LocalDate.parse(season1Info.get("end").asText());

        String description = name + " " + season;

        return new TournamentDTO(
                id,
                name,
                description,
                startDate,
                endDate,
                null,
                null,
                null,
                null
        );
    }

    protected LocalDate getInitialDate(TournamentExternalDTO.LeagueData apiFootballLeague) {
        if (apiFootballLeague.getSeasons() != null && !apiFootballLeague.getSeasons().isEmpty()) {
            return LocalDate.parse(apiFootballLeague.getSeasons().get(0).getStart());
        }
        return null;
    }

    protected LocalDate getFinalDate(TournamentExternalDTO.LeagueData apiFootballLeague) {
        if (apiFootballLeague.getSeasons() != null && !apiFootballLeague.getSeasons().isEmpty()) {
            return LocalDate.parse(apiFootballLeague.getSeasons().get(0).getEnd());
        }
        return null;
    }

    public TournamentStatsDTO toTournamentStatsDTO(Tournament tournament, Long leagueId, TournamentStatistics tournamentStatistics) {
        return new TournamentStatsDTO(
                leagueId,
                tournament.getName(),
                tournament.getDescription(),
                tournament.getTournament_logo(),
                tournamentStatistics.getWinnerTeamId(),
                tournamentStatistics.getFewestGoalsConcededTeamId(),
                tournamentStatistics.getTopScoringTeamId(),
                tournamentStatistics.getTopScorerId(),
                tournamentStatistics.getTopScorerName(),
                tournamentStatistics.getTopScorerTeamId(),
                tournamentStatistics.getTopScorerUrlImg()
        );
    }

    public Team map(Long teamId) {
        if (teamId == null) {
            return null;
        }
        Team team = new Team();
        team.setId(teamId);
        return team;
    }
}
