package co.edu.icesi.pollafutbolera.dto;

import java.util.List;

import lombok.Data;

@Data
public class TournamentExternalDTO {
    private List<LeagueData> response;

    @Data
    public static class LeagueData {
        private League league;
        private Country country;
        private List<Season> seasons;
        
        @Data
        public static class League {
            private Long id;
            private String name;
            private String type;
            private String logo;
        }

        @Data
        public static class Country {
            private String name;
            private String code;
            private String flag;
        }

        @Data
        public static class Season {
            private int year;
            private String start;
            private String end;
        }
    }
}