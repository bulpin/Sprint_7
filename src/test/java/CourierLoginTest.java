import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {
    private final String login = "testCourier";
    private final String password = "password123";

    @Before
    @Step("Создаём курьера перед авторизацией")
    public void setUp() {
        Courier courier = new Courier(login, password, "TestName");
        given()
                .spec(requestSpec)
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Test
    @Step("Проверяем авторизацию курьера")
    public void courierCanLogin() {
        CourierCredentials credentials = new CourierCredentials(login, password);
        Response response = given()
                .spec(requestSpec)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");
        response.then().statusCode(200).body("id", notNullValue());
    }
}


