package api;

import models.UserLoginModel;
import models.UserResponseModel;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AuthorizationApi {

    public UserResponseModel login(UserLoginModel user) {
        return given()
                .body(user)
                .contentType(JSON)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().as(UserResponseModel.class);
    }

    public void setAuthCookies(UserResponseModel userResponse) {
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", userResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", userResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", userResponse.getExpires()));
    }
}
