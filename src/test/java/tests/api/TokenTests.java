package tests.api;

import api.AccountApi;
import helpers.extensions.WithLogin;
import io.qameta.allure.*;
import models.GenerateTokenModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.time.Year;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Account")
@Feature("Генерация токена пользователя")
@Story("Я как пользователь хочу иметь возможность получать токен доступа после авторизации")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class TokenTests extends TestBaseApi {
    String year = Integer.toString(Year.now().getValue());
    AccountApi accountApi = new AccountApi();

    @Test
    @WithLogin
    @DisplayName("Проверка генерации токена")
    void getTokenTest() {

        GenerateTokenModel response = accountApi.generateTokenReturnResponse();

        step("Проверяем тело ответа", () -> {
            assertNotNull(response.getToken(), "Проверяем, что значение token не пусто");

            assertEquals("Success", response.getStatus(), "Проверяем значение status");

            assertEquals("User authorized successfully.", response.getResult(),
                    "Проверяем значение result");

            assertThat("Проверяем, что значение expires содержит текущий год",
                    response.getExpires(), containsString(year));
        });
    }
}