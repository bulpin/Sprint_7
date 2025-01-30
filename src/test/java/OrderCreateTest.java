import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Order Management")
@Feature("Create Order")
@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final String[] colors;

    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[] colors() {
        return new Object[]{
                new String[]{"BLACK"},
                new String[]{"GREY"},
                new String[]{"BLACK", "GREY"},
                new String[]{}
        };
    }

    @Test
    @Step("Создать заказ с цветами: {0}")
    public void orderCanBeCreatedWithColor() {
        String requestBody = colors.length > 0 ? "{ \"color\": [\"" + String.join("\", \"", colors) + "\"] }" : "{}";

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/api/v1/orders");

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
