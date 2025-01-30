import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    @Test
    @Step("Проверяем, что список заказов возвращается")
    public void orderListIsReturned() {
        given()
                .spec(requestSpec)
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
