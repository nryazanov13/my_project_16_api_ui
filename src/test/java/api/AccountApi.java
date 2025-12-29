package api;

import io.qameta.allure.Step;
import models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;
import static tests.TestBaseApi.STATIC_CORRECT_AUTH_DATA;

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

    @Step("Создание пользователя POST и возвращение ответа")
    public UserAccountResponseModel createUserWithCorrectUserData (UserAccountRequestModel userAccountRequestModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .body(userAccountRequestModel)
                .when()
                .post(USER)
                .then()
                .spec(responseSpec(201))
                .extract()
                .as(UserAccountResponseModel.class);
    }

    @Step("Создание НЕ корректного пользователя POST и возвращение ответа")
    public UserAccountErrorResponseModel createUserWithIncorrectAuthData(UserAccountRequestModel userAccountRequestModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .body(userAccountRequestModel)
                .when()
                .post(USER)
                .then()
                .spec(responseSpec(400))
                .extract()
                .as(UserAccountErrorResponseModel.class);
    }

    @Step("Удалить пользователя DELETE /Account/v1/User/{userId} – 200 с телом")
    public UserAccountErrorResponseModel deleteUserById(UserLoginResponseModel userResponseModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .delete(USER + "/{userId}", userResponseModel.getUserId())
                .then()
                // Swagger говорит, что при успехе статус 200
                .spec(responseSpec(200))
                .extract()
                .as(UserAccountErrorResponseModel.class);
    }

    @Step("Генерация токена POST " + GENERATE_TOKEN + ", Возвращаем response")
    public GenerateTokenModel generateTokenReturnResponse() {
        return given(baseRequestSpec)
                .contentType(JSON)
                .body(STATIC_CORRECT_AUTH_DATA)
                .when()
                .post(GENERATE_TOKEN)
                .then()
                .spec(responseSpec(200))
                .extract().as(GenerateTokenModel.class);
    }
}