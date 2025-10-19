package tests;

import org.openqa.selenium.Cookie;
import io.restassured.response.Response;
import models.UserLoginModel;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;

public class DemoQaTests {

    UserLoginModel user = new UserLoginModel("test123456", "Test123456@");

    @Test
    void successfulLoginWithUiTest() {
        open("/login");
        $("#username").setValue(user.getLogin());
        $("#password").setValue(user.getPassword()).pressEnter();
    }

    @Test
    void successfulLoginWithApiAndBooksTest() {

        Response authResponse = step("Login with a user", () ->
                given(baseRequestSpec)
                        .body(user)
                        .when()
                        .post("/Account/v1/login")
                        .then()
                        .spec(responseSpec(200))
                        .extract().response());

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204));

        String isbn = "83923839";
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                authResponse.path("userId") , isbn);

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201));

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");
        $(".ReactTable").shouldHave(text("Speaking JavaScript"));

    }
}
