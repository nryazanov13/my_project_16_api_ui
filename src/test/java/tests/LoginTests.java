package tests;

import io.restassured.response.Response;
import models.UserLoginModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;

public class LoginTests {

    @Test
    void successfulLoginWithUiTest(){

    }

    @Test
    void successfulLoginWithApiTest(){

        UserLoginModel user = new UserLoginModel("test123456","Test123456@");

        Response authResponse = step("Login with a user", () ->
                given(baseRequestSpec)
                        .body(user)
                        .when()
                        .post("/Account/v1/login")
                        .then()
                        .spec(responseSpec(201))
                        .extract().response());

                //assertNotNull(user));
    }
}
