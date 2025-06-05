package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.FixtureDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class FixtureUtil {

    public static List<FixtureDTO> fixture(){
        String json = "{\n" +
                "  \"get\": \"fixtures/statistics\",\n" +
                "  \"parameters\": {\n" +
                "    \"fixture\": \"1030303\",\n" +
                "    \"team\": \"541\"\n" +
                "  },\n" +
                "  \"errors\": [],\n" +
                "  \"results\": 1,\n" +
                "  \"paging\": {\n" +
                "    \"current\": 1,\n" +
                "    \"total\": 1\n" +
                "  },\n" +
                "  \"response\": [\n" +
                "    {\n" +
                "      \"team\": {\n" +
                "        \"id\": 541,\n" +
                "        \"name\": \"Real Madrid\",\n" +
                "        \"logo\": \"https://media.api-sports.io/football/teams/541.png\"\n" +
                "      },\n" +
                "      \"statistics\": [\n" +
                "        {\"type\": \"Shots on Goal\", \"value\": 5},\n" +
                "        {\"type\": \"Shots off Goal\", \"value\": 2},\n" +
                "        {\"type\": \"Total Shots\", \"value\": 13},\n" +
                "        {\"type\": \"Blocked Shots\", \"value\": 6},\n" +
                "        {\"type\": \"Shots insidebox\", \"value\": 7},\n" +
                "        {\"type\": \"Shots outsidebox\", \"value\": 6},\n" +
                "        {\"type\": \"Fouls\", \"value\": 11},\n" +
                "        {\"type\": \"Corner Kicks\", \"value\": 5},\n" +
                "        {\"type\": \"Offsides\", \"value\": 0},\n" +
                "        {\"type\": \"Ball Possession\", \"value\": \"60%\"},\n" +
                "        {\"type\": \"Yellow Cards\", \"value\": 0},\n" +
                "        {\"type\": \"Red Cards\", \"value\": null},\n" +
                "        {\"type\": \"Goalkeeper Saves\", \"value\": 1},\n" +
                "        {\"type\": \"Total passes\", \"value\": 602},\n" +
                "        {\"type\": \"Passes accurate\", \"value\": 533},\n" +
                "        {\"type\": \"Passes %\", \"value\": \"89%\"},\n" +
                "        {\"type\": \"expected_goals\", \"value\": null}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            // Extraer la lista de respuestas dentro de "response"
            JsonNode responseNode = rootNode.path("response");

            List<FixtureDTO> fixtures = objectMapper.readValue(responseNode.toString(), new TypeReference<List<FixtureDTO>>() {});
            return fixtures;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
