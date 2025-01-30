import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest extends BaseTest {
    private int courierId;
    private String uniqueLogin;

    @Before
    public void setUp() {
        uniqueLogin = "testCourier_" + System.currentTimeMillis();
        deleteCourierIfExists(uniqueLogin, "password123");
    }

    @Test
    @Step("Курьера можно создать")
    public void courierCanBeCreated() {
        Courier courier = new Courier(uniqueLogin, "password123", "TestName");
        Response response = createCourier(courier);
        response.then().statusCode(201).body("ok", equalTo(true));
        courierId = response.then().extract().path("id", String.valueOf(Integer.class));
    }

    @Test
    @Step("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourier() {
        Courier courier = new Courier(uniqueLogin, "password123", "TestName");
        createCourier(courier).then().statusCode(201);
        createCourier(courier).then().statusCode(409).body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @Step("Ошибка при создании курьера без обязательных полей")
    public void cannotCreateCourierWithoutRequiredFields() {
        Courier courierWithoutPassword = new Courier(uniqueLogin, "", "TestName");
        createCourier(courierWithoutPassword).then().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
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
}
