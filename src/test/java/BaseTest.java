import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Courier;
import models.CourierCredentials;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public abstract class BaseTest {
    protected static RequestSpecification requestSpec;

    @BeforeClass
    public static void setUpClass() {
        baseURI = "http://qa-scooter.praktikum-services.ru"; // Исправлено с https на http
        requestSpec = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setBaseUri(baseURI)
                .setContentType("application/json")
                .build();
    }

    protected Response createCourier(Courier courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    protected void deleteCourierIfExists(String login, String password) {
        CourierCredentials credentials = new CourierCredentials(login, password);
        Response response = given()
                .spec(requestSpec)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");

        if (response.statusCode() == 200) {
            Integer id = response.then().extract().path("id");
            if (id != null) {
                deleteCourier(id);
            }
        }
    }

    protected void deleteCourier(int id) {
        given()
                .spec(requestSpec)
                .when()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200);
    }
}