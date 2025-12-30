package api;

import io.qameta.allure.Step;
import models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;


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

    @Step("Получение ошибки профиля пользователя (ожидает 401)")
    public UserAccountErrorResponseModel getUserProfileError(UserLoginResponseModel userResponseModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .get(USER + "/{userId}", userResponseModel.getUserId())
                .then()
                .spec(responseSpec(401)) // Ожидает именно 401
                .extract()
                .as(UserAccountErrorResponseModel.class);
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

    @Step("Удалить пользователя DELETE /Account/v1/User/{userId} – 204 No Content")
    public void deleteUserById(UserLoginResponseModel userResponseModel) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .delete(USER + "/{userId}", userResponseModel.getUserId())
                .then()
                .spec(responseSpec(204));
    }

    @Step("Генерация токена для пользователя")
    public GenerateTokenModel generateTokenForUser(UserAccountRequestModel authData) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .body(authData)
                .when()
                .post(GENERATE_TOKEN)
                .then()
                .spec(responseSpec(200))
                .extract().as(GenerateTokenModel.class);
    }
}