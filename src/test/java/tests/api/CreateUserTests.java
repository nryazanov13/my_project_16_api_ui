package tests.api;

import helpers.extensions.WithLogin;
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
@Feature("Создание пользователя")
@Story("Я как пользователь хочу иметь возможность создавать нового пользователя в системе")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class CreateUserTests extends TestBaseApi {

    private static final String UUID_REGEX =
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    private static final String EXPECTED_ERROR_MESSAGE =
            "Passwords must have at least one non alphanumeric character, " +
                    "one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), " +
                    "one special character and Password must be eight characters or longer.";

    @Test
    @WithLogin
    @DisplayName("Проверка создания пользователя с корректными данными для авторизации")
    void createUserWithValidCredentials() {

        UserAccountResponseModel response = step(
                "Отправляем запрос на создание пользователя с рандомным именем и правильным паролем", () ->
                        userApi.createUserWithCorrectUserData(CORRECT_RANDOM_USERNAME_AUTH_DATA)
        );

        step("Валидируем тело ответа", () -> {

            assertThat("userID должен быть заполнен", response.getUserId(), notNullValue());
            assertThat("userID должен иметь UUID‑формат",
                    response.getUserId(),
                    matchesPattern(UUID_REGEX));
            assertThat("username в ответе должен совпадать с отправленным",
                    response.getUsername(),
                    equalTo(RANDOM_USERNAME));
            assertThat("список книг (books) должен присутствовать",
                    response.getBooks(),
                    notNullValue());
            assertThat("список книг должен быть пустым",
                    response.getBooks(),
                    hasSize(0));
        });
    }

    @Test
    @WithLogin
    @DisplayName("Проверка создания пользователя с НЕ корректными данными для авторизации")
    void createUserWithInValidCredentials() {

        UserAccountErrorResponseModel errorResponse = step(
                "Отправляем запрос на создание пользователя с неправильным паролем", () ->
                        userApi.createUserWithIncorrectAuthData(INCORRECT_AUTH_DATA)
        );

        step("Валидируем тело ошибки", () -> {
            assertThat("поле code должно быть равно 1300", errorResponse.getCode(), equalTo(1300));
            assertThat("поле message должно соответствовать ожидаемому",
                    errorResponse.getMessage(), equalTo(EXPECTED_ERROR_MESSAGE));
        });
    }

}
