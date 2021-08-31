import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

public class TestConfig {

    @BeforeClass
    public static void setUp() {

        //setting proxy (then if fiddler is installed it is possible to read all traffic there,
        // it will be directed to through this proxy)
//        RestAssured.proxy("localhost", 8888);

        //rest assured configuration on default level
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/app/";
        RestAssured.port = 8080;

        //RequestSpecification enables adding header, cookies, form parameters, base url, port etc. instead of default ones
        //.build() method used to add it as RequestSpecBuilder()
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        //adding request to RestAssured (now after running test, they are used and displayed in console)
        RestAssured.requestSpecification = requestSpecification;

        //ResponseSpecification: check status code, check content type, response time, check headers
        //.build() method used to add it as ResponseSpecBuilder()
        //response is displayed in log
        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200) // if it will be different in the response, there will be an error (test will fail)
                .build();

        RestAssured.responseSpecification = responseSpecification;
    }
}
