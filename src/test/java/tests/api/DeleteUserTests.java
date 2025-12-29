package tests.api;


import io.qameta.allure.*;
import models.*;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Epic("Account")
@Feature("Удаление пользователя")
@Story("Я как пользователь хочу иметь возможность удалить своего пользователя и увидеть корректный ответ")
@Severity(SeverityLevel.CRITICAL)
@Owner("Nikita Ryazanov")
@Tag("api")
public class DeleteUserTests extends TestBaseApi {

    @Test
    @DisplayName("Успешное удаление только что созданного пользователя")
    void deleteUser_successfulResponse() {

        UserAccountResponseModel created = step(
                "Отправляем запрос POST /Account/v1/User с рандомным именем и валидным паролем",
                () -> userApi.createUserWithCorrectUserData(CORRECT_RANDOM_USERNAME_AUTH_DATA)
        );

        UserAccountErrorResponseModel deleteResp = step(
                "Отправляем DELETE /Account/v1/User/{userId} для удаления пользователя по Id",
                () -> userApi.deleteUserById(userRandomResponse)
        );

        step("Валидируем тело ответа", () -> {
            assertThat("code должен быть 0 (успешное удаление)",
                    deleteResp.getCode(), equalTo(0));

            assertThat("message не должен быть пустым",
                    deleteResp.getMessage(),
                    allOf(notNullValue(), not(emptyOrNullString())));
        });
    }
}