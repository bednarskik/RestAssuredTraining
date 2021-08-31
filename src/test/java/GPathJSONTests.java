import config.FootballApiConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;

public class GPathJSONTests extends FootballApiConfig {

    @Test
    public void extractMapOfElementsWithFind() {
        Response response = get("competitions/2021/teams");
        // find method to extract data for one team from endpoint: http://api.football-data.org/v2/competitions/2021/teams
        Map<String, ?> allTeamDataForSingleTeam = response.path("teams.find {it.name == 'Manchester United FC' }");

        System.out.println("Map of Team Data = " + allTeamDataForSingleTeam);
    }

    @Test
    public void extractSingleValueWithFind() {
        Response response = get("teams/57");
        String certainPlayer = response.path("squad.find {it.id == 7797}.name"); //single element extracted with "find" method
        System.out.println("Name of player " + certainPlayer);
    }

    @Test
    public void extractListOfValuesWithFindAll() {
        Response response = get("teams/57");
        List<String> playerNames = response.path("squad.findAll {it.id >= 1}.name"); //all elements extracted with "findAll" method
        System.out.println("List of players " + playerNames);
    }

    @Test
    public void extractSingleValueWithHighestNumber() {
        Response response = get("teams/57");
        String playerName = response.path("squad.max {it.id}.name"); //single elements extracted with "max" player Id
        System.out.println("PLayer with highest id: " + playerName);
    }

    @Test
    public void extractMultipleValuesAndSumThem() {
        Response response = get("teams/57");
        int sumOfIds = response.path("squad.collect {it.id}.sum()"); //sum of iDs extracted with "sum()" method
        System.out.println("Sum of the Ids: " + sumOfIds);
    }

    @Test
    public void extractMapOfObjectsWithFindAndFindAll() {
        Response response = get("teams/57");
        Map<String, ?> playerOfZCertainPosition = response.path(
                "squad.findAll {it.position == 'Defender' }.find {it.nationality == 'Portugal'}");
        System.out.println("Details of players of = " + playerOfZCertainPosition);
    }

    @Test
    public void extractMapOfObjectsWithFindAndFindAllWithParameters() {
        String position = "Defender";
        String nationality = "Portugal";
        Response response = get("teams/57");
        Map<String, ?> playerOfZCertainPosition = response.path(
                "squad.findAll {it.position == '%s' }.find {it.nationality == '%s'}",
                position, nationality);
        System.out.println("Details of players of = " + playerOfZCertainPosition);
    }

    @Test
    public void extractMultiplePlayers() {
        String position = "Midfielder";
        String nationality = "England";
        Response response = get("teams/57");
        ArrayList<Map<String, ?>> allPlayersCertainNation = response.path(
                "squad.findAll {it.position == '%s' }.findAll {it.nationality == '%s'}",
                position, nationality);
        System.out.println("All players = " + allPlayersCertainNation);
    }
}
