package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import co.edu.icesi.pollafutbolera.dto.StatisticDTO;
import co.edu.icesi.pollafutbolera.model.MatchStats;
import co.edu.icesi.pollafutbolera.repository.MatchRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

@Mapper(componentModel = "spring")
public interface MatchStatsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matchId", ignore = true)
    @Mapping(target = "teamName", source = "team.name")
    @Mapping(target = "logo", source = "team.logo")
    MatchStats toMatchStats(FixtureDTO fixtureDTO);

    @Mapping(target = "team.id", ignore = true)
    @Mapping(target = "team.name", source = "teamName")
    @Mapping(target = "team.logo", source = "logo")
    @Mapping(target = "statistics", ignore = true)
    FixtureDTO toFixtureDTO(MatchStats matchStats);



    default MatchStats completeMatchStats(FixtureDTO fixtureDTO) {
        MatchStats matchStats = toMatchStats(fixtureDTO);

        if (fixtureDTO.getStatistics() != null) {
            Map<String, String> statMap = new HashMap<>();
            for (StatisticDTO stat : fixtureDTO.getStatistics()) {
                statMap.put(toCamelCase(stat.getType()), stat.getValue());
            }

            for (Field field : MatchStats.class.getDeclaredFields()) {
                String fieldName = field.getName();
                if (fieldName.equals("matchId") || fieldName.equals("id")) continue;

                if (statMap.containsKey(fieldName)) {
                    field.setAccessible(true);
                    try {
                        String value = statMap.get(fieldName);
                        if (field.getType().equals(Integer.class)) {
                            field.set(matchStats, parseInteger(value));
                        } else if (field.getType().equals(String.class)) {
                            field.set(matchStats, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error al mapear el campo: " + fieldName, e);
                    }
                }
            }
        }

        return matchStats;
    }

    default FixtureDTO completeFixtureDTO(MatchStats matchStats) {
        FixtureDTO dto = toFixtureDTO(matchStats);
        List<StatisticDTO> statistics = new ArrayList<>();

        for (Field field : MatchStats.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (isStatField(fieldName)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(matchStats);

                    if (value != null) {
                        StatisticDTO stat = new StatisticDTO();
                        stat.setType(toStatType(fieldName));
                        stat.setValue(value.toString());
                        statistics.add(stat);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error al leer el campo: " + fieldName, e);
                }
            }
        }

        dto.setStatistics(statistics);
        return dto;
    }

    // Métodos auxiliares que no deben ser confundidos con mapeos directos
    private static Integer parseInteger(String value) {
        try {
            if (value == null) return null;
            String numeric = value.replaceAll("[^\\d]", "");
            return numeric.isEmpty() ? null : Integer.parseInt(numeric);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String toCamelCase(String input) {
        if (input == null) return null;
        String[] parts = input.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }
        String str = sb.toString();
        if(str.equals("passes%")){
            str = str.replace("%", "Percentage");
        }
        return str;
    }

    private static String toStatType(String camelCase) {
        String type = camelCase.replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceFirst("^.", String.valueOf(camelCase.charAt(0)).toUpperCase());

        if(type.equals("Passes Percentage")){
            type = type.replace("Percentage", "%");
        }
        return type;
    }

    private static boolean isStatField(String fieldName) {
        return !List.of("id", "matchId", "teamName", "logo").contains(fieldName);
    }
}

