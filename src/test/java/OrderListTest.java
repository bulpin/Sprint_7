import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest extends BaseTest {

    @Test
    @Step("Проверяем, что список заказов возвращается")
    public void orderListIsReturned() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/api/v1/orders");

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}



