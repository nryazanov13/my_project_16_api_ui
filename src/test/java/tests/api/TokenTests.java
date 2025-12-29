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
import static org.junit.jupiter.api.Assertions.*;

@Epic("Account")
@Feature("Генерация токена пользователя")
@Story("Я как пользователь хочу иметь возможность получать токен доступа после авторизации")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class TokenTests extends TestBaseApi {
    String currentYear = String.valueOf(Year.now().getValue());
    String nextYear = String.valueOf(Year.now().plusYears(1).getValue());
    AccountApi accountApi = new AccountApi();

    @Test
    @WithLogin
    @DisplayName("Проверка генерации токена")
    void getTokenTest() {

        GenerateTokenModel response = accountApi.generateTokenReturnResponse();

        boolean containsYear = response.getExpires().contains(currentYear) ||
                response.getExpires().contains(nextYear);

        step("Проверяем тело ответа", () -> {

            assertNotNull(response.getToken(), "Проверяем, что значение token не пусто");

            assertEquals("Success", response.getStatus(), "Проверяем значение status");

            assertEquals("User authorized successfully.", response.getResult(),
                    "Проверяем значение result");

            assertTrue(containsYear,
                    () -> "expires должен содержать текущий (" + currentYear + ") или следующий (" + nextYear + ") год, а получено: "
                            + response.getExpires());
        });
    }
}