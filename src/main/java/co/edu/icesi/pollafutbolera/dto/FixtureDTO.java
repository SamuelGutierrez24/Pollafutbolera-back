package co.edu.icesi.pollafutbolera.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixtureDTO {
    private TeamStaticsDTO team;
    private List<StatisticDTO> statistics;
}