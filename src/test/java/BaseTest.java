import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.baseURI;

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
}
