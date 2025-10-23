package helpers.extensions;

import models.UserLoginModel;
import models.UserResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import tests.TestBase;

public class LoginExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        // Используем статический authorizationApi из TestBase
        UserLoginModel user = new UserLoginModel("test111", "Test1234567!");
        UserResponseModel userResponse = TestBase.authorizationApi.login(user);
        TestBase.authorizationApi.setAuthCookies(userResponse);
    }
}
