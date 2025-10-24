package api;

import models.UserModel;
import models.UserLoginResponseModel;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AuthorizationApi {

    public UserLoginResponseModel login(UserModel user) {
        return given()
                .body(user)
                .contentType(JSON)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().as(UserLoginResponseModel.class);
    }

    public void setAuthCookies(UserLoginResponseModel userResponse) {
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", userResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", userResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", userResponse.getExpires()));
    }
}
