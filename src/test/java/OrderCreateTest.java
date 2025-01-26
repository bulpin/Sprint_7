import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Order Management")
@Feature("Create Order")
@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final String color;

    public OrderCreateTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] colors() {
        return new Object[]{"BLACK", "GREY", null};
    }

    @Test
    @Step("Создать заказ с цветом: {0}")
    public void orderCanBeCreatedWithColor() {
        String requestBody = color != null ? "{ \"color\": [\"" + color + "\"] }" : "{}";

        given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
