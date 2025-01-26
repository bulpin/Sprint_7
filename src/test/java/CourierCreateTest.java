import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {
    private int courierId;

    @Test
    @Step("Проверяем создание курьера")
    public void courierCanBeCreated() {
        Courier courier = new Courier("testCourier", "password123", "TestName");
        Response response = createCourier(courier);
        response.then().statusCode(201).body("ok", equalTo(true));
        courierId = response.then().extract().path("id");
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

    @Step("Удаляем курьера")
    private void deleteCourier(int id) {
        given()
                .spec(requestSpec)
                .when()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200);
    }
}