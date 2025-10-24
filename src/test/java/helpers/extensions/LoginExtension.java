package helpers.extensions;

import models.UserModel;
import models.UserLoginResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import tests.TestBase;

import static tests.TestBase.authorizationApi;

public class LoginExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        // Используем статический authorizationApi из TestBase
        UserModel user = new UserModel("test111", "Test1234567!");
        UserLoginResponseModel userResponse = authorizationApi.login(user);

        if (context.getTestInstance().isPresent()) {
            Object testInstance = context.getTestInstance().get();
            if (testInstance instanceof TestBase) {
                TestBase base = (TestBase) testInstance;
                base.setUserResponse(userResponse);
                base.setUser(user); //
            }
        }
    }
}
