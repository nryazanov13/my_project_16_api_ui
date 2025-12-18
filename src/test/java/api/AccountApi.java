package api;

import io.qameta.allure.Step;
import models.GenerateTokenModel;
import models.UserAccountResponseModel;
import models.UserLoginResponseModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;
import static tests.TestBase.AUTH_DATA;

public class AccountApi {

    private static final String USER = "/Account/v1/User";
    private static final String GENERATE_TOKEN = "/Account/v1/GenerateToken";

    @Step("Получения профиля пользователя GET и возвращение ответа")
    public UserAccountResponseModel getUserProfile(UserLoginResponseModel userResponseModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .get(USER + "/{userId}", userResponseModel.getUserId())
                .then()
                .spec(responseSpec(200))
                .extract()
                .as(UserAccountResponseModel.class);
    }

    @Step("Генерация токена POST " + GENERATE_TOKEN + ", Возвращаем response")
    public GenerateTokenModel generateTokenReturnResponse() {
        return given(baseRequestSpec)
                .body(AUTH_DATA)
                .when()
                .post(GENERATE_TOKEN)
                .then()
                .spec(responseSpec(200))
                .extract().as(GenerateTokenModel.class);
    }
}
