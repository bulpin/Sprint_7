import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.CourierCredentials;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public abstract class BaseTest {
    protected static RequestSpecification requestSpec;

    @BeforeClass
    public static void setUpClass() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
        requestSpec = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setBaseUri(baseURI)
                .setContentType("application/json")
                .build();
    }

    protected void deleteCourierIfExists(String login, String password) {
        CourierCredentials credentials = new CourierCredentials(login, password);
        Response response = given()
                .spec(requestSpec)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");

        if (response.statusCode() == 200) {
            Integer courierId = response.then().extract().path("id", String.valueOf(Integer.class));
            if (courierId != null) {
                deleteCourier(courierId);
            }
        }
    }

    protected void deleteCourier(int id) {
        Response response = given()
                .spec(requestSpec)
                .when()
                .delete("/api/v1/courier/" + id);

        if (response.statusCode() == 200) {
            System.out.println("Курьер с ID " + id + " успешно удален.");
        } else {
            System.out.println("Не удалось удалить курьера с ID " + id + ". Код ответа: " + response.statusCode());
        }
    }
}
