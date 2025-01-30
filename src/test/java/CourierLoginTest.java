import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {
    private String login;
    private final String password = "password123";
    private int courierId;

    @Before
    @Step("Создаём курьера перед авторизацией")
    public void setUp() {
        login = "testCourier_" + System.currentTimeMillis();
        deleteCourierIfExists(login, password);
        Courier courier = new Courier(login, password, "TestName");
        Response response = createCourier(courier);
        response.then().statusCode(201);
        courierId = response.then().extract().path("id", String.valueOf(Integer.class));
    }

    @Test
    @Step("Проверяем авторизацию курьера")
    public void courierCanLogin() {
        CourierCredentials credentials = new CourierCredentials(login, password);
        Response response = loginCourier(credentials);
        response.then().statusCode(200).body("id", notNullValue());
    }

    @Test
    @Step("Ошибка при авторизации без пароля")
    public void cannotLoginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials(login, "");
        loginCourier(credentials).then().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Ошибка при авторизации с неверными данными")
    public void cannotLoginWithInvalidCredentials() {
        CourierCredentials credentials = new CourierCredentials(login, "wrongPassword");
        loginCourier(credentials).then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Step("Создаём курьера")
    private Response createCourier(Courier courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логиним курьера")
    private Response loginCourier(CourierCredentials credentials) {
        return given()
                .spec(requestSpec)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");
    }
}
