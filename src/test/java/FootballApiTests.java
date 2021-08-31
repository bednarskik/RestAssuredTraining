import config.FootballApiConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FootballApiTests extends FootballApiConfig {

    // path parameter in URL > {videoGameId}
    // query parameter in URL > ?videoGameId=
    @Test //with query parameter
    public void getDetailsOfOneArea() {
        given().
                queryParam("areas", 2072).
        when().
                get("areas").
        then();
    }

    @Test
    public void getDateFounded() {
        given().
        when().
                get("teams/57").
        then().
                body("founded", equalTo(1886));
    }

    @Test //assert body of HTTP response
    public void getFirstTeamName() {
        given().
        when().
                get("competitions/2021/teams").
        then().
                body("teams.name[0]", equalTo("Arsenal FC"));
    }

    @Test //extract body of HTTP response
    public void getAllTeamData() {
        String responseBody = get("teams/57").asString();
        System.out.println(responseBody);
    }

    @Test
    public void getAllTeamData_DoCheckFirst() {
        Response response =
                given().
                when().
                        get("teams/57").
                then().
                        contentType(ContentType.JSON).
                        extract().response();

        String jsonResponseAsString = response.asString();
        System.out.println("!!!!!! " + jsonResponseAsString);
    }

    @Test //extract HTTP headers
    public void extractHeaders() {
        Response response =
                given().
                        when().
                        get("teams/57").
                        then().
                        contentType(ContentType.JSON).
                        extract().response();

        //all headers extracted
        Headers headers = response.getHeaders();
        System.out.println("All headers\n" + headers);

        //single header extracted
        String header = response.getHeader("Content-Type");
        System.out.println("Single header\n" + header);
    }

    @Test //JSON Path to extract explicit data
    public void extractFirstTeamName() {
        String firstTeamName = get("competitions/2021/teams").jsonPath().getString("teams.name[0]");
        System.out.println("!!!!!! First team name: " + firstTeamName);
    }

    @Test
    public void allTeamNames() {
        Response response =
                given().
                when().when().
                        get("competitions/2021/teams").
                then().extract().response();

        List<String> teamNames = response.path("teams.name");

        System.out.println("ALL TEAM NAMES!!!!!!");
        for (String teamName : teamNames) {
            System.out.println(teamName);
        }
    }
}
