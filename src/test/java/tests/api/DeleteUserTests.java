package tests.api;

import io.qameta.allure.*;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Account")
@Feature("Удаление пользователя")
@Story("Я как пользователь хочу иметь возможность удалить своего пользователя")
@Severity(SeverityLevel.CRITICAL)
@Owner("Nikita Ryazanov")
@Tag("api")
public class DeleteUserTests extends TestBaseApi {

    @Test
    @DisplayName("Успешное удаление пользователя с проверкой кода 204 и ошибки 401 при повторном запросе пользователя")
    void deleteUser_successful() {

        String randomUsername = getRandomUsername();
        UserAccountRequestModel authData =
                new UserAccountRequestModel(randomUsername, CORRECT_PASSWORD);

        UserAccountResponseModel createdUser = step(
                "Создаем нового пользователя",
                () -> userApi.createUserWithCorrectUserData(authData)
        );

        GenerateTokenModel tokenResponse = step(
                "Генерируем токен для пользователя",
                () -> userApi.generateTokenForUser(authData)
        );

        UserLoginResponseModel userAuth = UserLoginResponseModel.builder()
                .userId(createdUser.getUserId())
                .token(tokenResponse.getToken())
                .build();

        step("Проверяем существование пользователя", () -> {
            UserAccountResponseModel profile = userApi.getUserProfile(userAuth);
            assertThat("Пользователь должен существовать", profile, notNullValue());
            assertThat("ID должен совпадать",
                    profile.getUserId(), equalTo(createdUser.getUserId()));
        });

        step("Удаляем пользователя (ожидаем 204 No Content)", () -> {
            userApi.deleteUserById(userAuth);
        });

        step("Проверяем что пользователь удален и тело ответа", () -> {
            UserAccountErrorResponseModel error = userApi.getUserProfileError(userAuth);

            assertThat("Код ошибки должен быть '1207'",
                    error.getCode(), equalTo("1207"));
            assertThat("Сообщение об ошибке должно быть 'User not found!'",
                    error.getMessage(), equalTo("User not found!"));
        });
    }
}