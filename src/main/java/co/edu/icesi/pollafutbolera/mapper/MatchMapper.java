package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.MatchDTO;
import co.edu.icesi.pollafutbolera.model.Match;
import co.edu.icesi.pollafutbolera.model.Stage;
import co.edu.icesi.pollafutbolera.model.Team;
import co.edu.icesi.pollafutbolera.model.Tournament;
import co.edu.icesi.pollafutbolera.repository.StageRepository;
import co.edu.icesi.pollafutbolera.repository.TeamRepository;
import co.edu.icesi.pollafutbolera.repository.TournamentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MatchMapper {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private StageRepository stageRepository;

    @Mapping(source = "homeTeam.id", target = "homeTeamId")
    @Mapping(source = "awayTeam.id", target = "awayTeamId")
    @Mapping(source = "winnerTeam.id", target = "winnerTeamId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "stage.id", target = "stageId")
    @Mapping(source = "homeScore", target = "homeScore")
    @Mapping(source = "awayScore", target = "awayScore")
    @Mapping(source = "extratime", target = "extratime")
    @Mapping(source = "extraHomeScore", target = "extraHomeScore")
    @Mapping(source = "extraAwayScore", target = "extraAwayScore")
    @Mapping(source = "penalty", target = "penalty")
    @Mapping(source = "penaltyHome", target = "penaltyHome")
    @Mapping(source = "penaltyAway", target = "penaltyAway")
    public abstract MatchDTO toMatchDTO(Match entity);

    @Mapping(target = "homeTeam", expression = "java(mapTeam(matchDTO.homeTeamId()))")
    @Mapping(target = "awayTeam", expression = "java(mapTeam(matchDTO.awayTeamId()))")
    @Mapping(target = "winnerTeam", expression = "java(mapTeam(matchDTO.winnerTeamId()))")
    @Mapping(target = "tournament", expression = "java(mapTournament(matchDTO.tournamentId()))")
    @Mapping(target = "stage", expression = "java(mapStage(matchDTO.stageId()))")
    @Mapping(target = "homeScore", source = "homeScore")
    @Mapping(target = "awayScore", source = "awayScore")
    @Mapping(target = "extratime", source = "extratime")
    @Mapping(target = "extraHomeScore", source = "extraHomeScore")
    @Mapping(target = "extraAwayScore", source = "extraAwayScore")
    @Mapping(target = "penalty", source = "penalty")
    @Mapping(target = "penaltyHome", source = "penaltyHome")
    @Mapping(target = "penaltyAway", source = "penaltyAway")
    public abstract Match toMatch(MatchDTO matchDTO);

    @Mapping(source = "homeTeam.id", target = "homeTeamId")
    @Mapping(source = "awayTeam.id", target = "awayTeamId")
    @Mapping(source = "winnerTeam.id", target = "winnerTeamId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "stage.id", target = "stageId")
    @Mapping(source = "homeScore", target = "homeScore")
    @Mapping(source = "awayScore", target = "awayScore")
    @Mapping(source = "extratime", target = "extratime")
    @Mapping(source = "extraHomeScore", target = "extraHomeScore")
    @Mapping(source = "extraAwayScore", target = "extraAwayScore")
    @Mapping(source = "penalty", target = "penalty")
    @Mapping(source = "penaltyHome", target = "penaltyHome")
    @Mapping(source = "penaltyAway", target = "penaltyAway")
    public abstract List<MatchDTO> listToMatchDTO(List<Match> entity);

    @Mapping(target = "homeTeam", expression = "java(mapTeam(matchDTO.homeTeamId()))")
    @Mapping(target = "awayTeam", expression = "java(mapTeam(matchDTO.awayTeamId()))")
    @Mapping(target = "winnerTeam", expression = "java(mapTeam(matchDTO.winnerTeamId()))")
    @Mapping(target = "tournament", expression = "java(mapTournament(matchDTO.tournamentId()))")
    @Mapping(target = "stage", expression = "java(mapStage(matchDTO.stageId()))")
    @Mapping(target = "homeScore", source = "homeScore")
    @Mapping(target = "awayScore", source = "awayScore")
    @Mapping(target = "extratime", source = "extratime")
    @Mapping(target = "extraHomeScore", source = "extraHomeScore")
    @Mapping(target = "extraAwayScore", source = "extraAwayScore")
    @Mapping(target = "penalty", source = "penalty")
    @Mapping(target = "penaltyHome", source = "penaltyHome")
    @Mapping(target = "penaltyAway", source = "penaltyAway")
    public abstract List<Match> listToMatch(List<MatchDTO> entity);


    @Named("fromJson")
    public MatchDTO fromJson(JsonNode json) {

        JsonNode fixtureNode = json.get("fixture");
        Long id = fixtureNode.get("id").asLong();

        // Obtener la fecha y hora
        ZonedDateTime utcDateTime = ZonedDateTime.parse(fixtureNode.get("date").asText(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime colombiaTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/Bogota"));
        LocalDateTime date = colombiaTime.toLocalDateTime();

        //STATUS
        String status = fixtureNode.get("status").get("long").asText();

        // Obtener los equipos y su información
        JsonNode teamsNode = json.get("teams");
        String homeTeam = teamsNode.get("home").get("name").asText(); //Hacerle findByName como stage
        String homeTeamLogo = teamsNode.get("home").get("logo").asText();
        Long homeId = teamsNode.get("home").get("id").asLong();
        Long homeTeamId = mapTeamId(homeId,homeTeam,homeTeamLogo);
        String awayTeam = teamsNode.get("away").get("name").asText();
        String  awayTeamLogo = teamsNode.get("away").get("logo").asText();
        Long awayId = teamsNode.get("away").get("id").asLong();
        Long awayTeamId = mapTeamId(awayId, awayTeam,awayTeamLogo);


        Long winnerTeamId = null;
        if (teamsNode.get("home").get("winner").asBoolean()) {
            winnerTeamId = homeTeamId;
        } else if (teamsNode.get("away").get("winner").asBoolean()) {
            winnerTeamId = awayTeamId;
        }


        // Obtener la liga y la fase
        JsonNode leagueNode = json.get("league");
        String tournamentName = leagueNode.get("name").asText();
        Long tornamentId = leagueNode.get("id").asLong();
        Long tournamentId = mapTournamentId(tornamentId,tournamentName);

        String round = leagueNode.get("round").asText();
        System.out.println("Round: " + round);
        Long stageId = mapStageId(round,tournamentId);
        System.out.println("Stage ID: " + stageId);


        // obtener marcador
        JsonNode goalsNode = json.get("goals");
        Integer homeScore = goalsNode.get("home").asInt();
        Integer awayScore = goalsNode.get("away").asInt();

        // obtener tiempo extra

        JsonNode extraTimeNode = json.get("score").get("extratime");

        boolean extratime = false;
        Integer extraHomeScore = null;
        Integer extraAwayScore = null;

        if (extraTimeNode != null && !extraTimeNode.get("home").isNull() && !extraTimeNode.get("away").isNull()) {
            extratime = true;
            extraHomeScore = extraTimeNode.get("home").asInt();
            extraAwayScore = extraTimeNode.get("away").asInt();
        }

        // obtener penales

        JsonNode penaltyNode = json.get("score").get("penalty");
        boolean penalty = false;
        Integer penaltyHome = null;
        Integer penaltyAway = null;

        if (penaltyNode != null && !penaltyNode.get("home").isNull() && !penaltyNode.get("away").isNull()) {
            penalty = true;
            penaltyHome = penaltyNode.get("home").asInt();
            penaltyAway = penaltyNode.get("away").asInt();
        }




        return MatchDTO.builder()
                .id(id)
                .date(date)
                .status(status)
                .homeTeamId(homeTeamId)
                .awayTeamId(awayTeamId)
                .winnerTeamId(winnerTeamId)
                .tournamentId(tournamentId)
                .stageId(stageId)
                .deletedAt(null)
                .homeScore(homeScore)
                .awayScore(awayScore)
                .extratime(extratime)
                .extraHomeScore(extraHomeScore)
                .extraAwayScore(extraAwayScore)
                .penalty(penalty)
                .penaltyHome(penaltyHome)
                .penaltyAway(penaltyAway)
                .build();
    }

    private Long mapStageId(String round, Long tournamentId) {
        Optional<Stage> stage = stageRepository
                .findFirstByTournament_IdAndNameIgnoreCase(tournamentId, round    );
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if(stage.isEmpty()){
            Stage stage1 = new Stage();
            stage1.setName(round);
            stage1.setTournament(tournament.get());
            stageRepository.save(stage1);
            stage = stageRepository.findByName(round);
        }
        return stage.get().getId();
    }

    private Long mapTeamId(Long id,String team, String logo) {
        Optional<Team> team1 = teamRepository.findByName(team);
        if(team1.isEmpty()){
            Team team2 = new Team();
            team2.setId(id);
            team2.setName(team);
            team2.setLogoUrl(logo);
            teamRepository.save(team2);
            team1 = teamRepository.findByName(team);
        }
        return team1.get().getId();
    }
    private Long mapTournamentId(Long id,String tournament) {
        Optional<Tournament> tournament1 = tournamentRepository.findById(id);
        if (tournament1.isEmpty()){
            Tournament tournament2 = new Tournament();
            tournament2.setId(id);
            tournament2.setName(tournament);
            tournament2.setDescription("Ejemplo");
            System.out.println(tournament2);
            tournamentRepository.saveAndFlush(tournament2);
            tournament1 = tournamentRepository.findByName(tournament);
        }
        return tournament1.get().getId();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromDTO(MatchDTO permissionDTO, @MappingTarget Match match);

    protected Team mapTeam(Long teamId) {
        if (teamId == null) return null;
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
    }

    protected Tournament mapTournament(Long tournamentId) {
        if (tournamentId == null) return null;
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));
    }

    protected Stage mapStage(Long stageId) {
        if (stageId == null) return null;
        return stageRepository.findById(stageId)
                .orElseThrow(() -> new IllegalArgumentException("Stage not found with ID: " + stageId));
    }
}