import config.VideoGameConfig;
import config.VideoGamesEndpoints;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

public class VideoGameDbTests extends VideoGameConfig {
    //HTTP verbs:
    //GET > retrieve data from an endpoint
    //POST > send data to an endpoint
    //PUT > update a resource
    //DELETE > delete a resource

    @Test
    public void getAllGames() {
        given()
        .when().get(VideoGamesEndpoints.ALL_VIDEO_GAMES)
        .then();
    }

    @Test
    public void createNewGameByJSON() {
        String gameBodyJson = "{\n" +
                "  \"id\": 11,\n" +
                "  \"name\": \"NewGame\",\n" +
                "  \"releaseDate\": \"2021-03-28T16:39:15.901Z\",\n" +
                "  \"reviewScore\": 88,\n" +
                "  \"category\": \"Shooter\",\n" +
                "  \"rating\": \"Mature\"\n" +
                "}";

        given()
                .body(gameBodyJson).
        when()
                .post(VideoGamesEndpoints.ALL_VIDEO_GAMES).
        then();
    }

    @Test
    public void createNewGameByXML() {
        String gameBodyXml = "<videoGame category=\"Shooter\" rating=\"Universal\">\n" +
                "    <id>12</id>\n" +
                "    <name>Resident Evil 8</name>\n" +
                "    <releaseDate>2005-10-01T00:00:00+02:00</releaseDate>\n" +
                "    <reviewScore>85</reviewScore>\n" +
                "  </videoGame>";

        given()
                .body(gameBodyXml)
                .header("Accept", "application/xml")
                .header("Content-Type", "application/xml").
        when().
                post(VideoGamesEndpoints.ALL_VIDEO_GAMES).
        then();
    }

    @Test
    public void updateGameByJSON() {
        String gameBodyJson = "{\n" +
                "  \"id\": 12,\n" +
                "  \"name\": \"Updated Name\",\n" +
                "  \"releaseDate\": \"2021-04-02T21:10:55.566Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"string\",\n" +
                "  \"rating\": \"string\"\n" +
                "}";
        given()
                .body(gameBodyJson).
        when().
                put("videogames/12")
        .then();
    }

    @Test
    public void deleteGame() {
        given().
        when().
                delete("videogames/9").
        then();
    }

    // path parameter in URL > {videoGameId}
    // query parameter in URL > ?videoGameId=
    @Test //with path parameters
    public void getSingleGame() {
        given().
                pathParam("videoGameId", "8").
        when().get(VideoGamesEndpoints.SINGLE_VIDEO_GAME).
        then();
    }

    // serialization is converting JSON object into POJO (plain old java object)
    //get a JSON object > convert to a POJO > Use POJO in Rest Assured code to create new game
    //http://pojo.sodhanalibrary.com/
    @Test
    public void testVideoGameSerializationByJson() {
        //creating object with Video Game
        VideoGame videoGame = new VideoGame("99", "2018-04-04", "My awesome game", "Mature", "16", "Shooter");
        //serialize game to JSON is done by rest assured
        given().
                body(videoGame).
        when().
                post(VideoGamesEndpoints.ALL_VIDEO_GAMES).
        then();
    }
    //generate XML object > generate XSD schema > place the schema in our project > run test that validates against schema
    //https://www.freeformatter.com/xsd-generator.html#ad-output
    @Test
    public void testVideoGameSchemaXML() {
        given().
                pathParam("videoGameId", 5).
                header("Accept", "application/xml").
                header("Content-Type", "application/xml").
        when().
                get(VideoGamesEndpoints.SINGLE_VIDEO_GAME).
        then()
                .body(matchesXsdInClasspath("VideoGameXSD.xsd"));
    }
    //generate JSON object > generate JSON schema > place the schema in our project > run test that validates against schema
    //https://www.liquid-technologies.com/online-json-to-schema-converter
    @Test
    public void testVideoGameSchemaJson() {
        given().
                pathParam("videoGameId", 5).
        when().
                get(VideoGamesEndpoints.SINGLE_VIDEO_GAME).
        then()
                .body(matchesJsonSchemaInClasspath("VideoGameJsonSchema.json"));
    }

    @Test
    public void convertJsonToPojo() {
        Response response = given().pathParam("videoGameId", 5).
                when().
                get(VideoGamesEndpoints.SINGLE_VIDEO_GAME);

        VideoGame videoGame = response.getBody().as(VideoGame.class);

        System.out.println(videoGame.toString());
    }

    //capture response time > assert on response time > add response time assertion to response specification
    @Test
    public void captureResponseTime() {
        long responseTime  = given().get(VideoGamesEndpoints.ALL_VIDEO_GAMES).time();
        System.out.println("Response time in miliseconds: " + responseTime);
    }

    @Test
    public void assertOnResponseTime() {
        when().
                get(VideoGamesEndpoints.ALL_VIDEO_GAMES).
        then().
                time(lessThan(1000L));
    }
}
