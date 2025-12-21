package helpers.extensions;

import models.UserLoginResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import tests.TestBaseApiUi;

import static tests.TestBaseApiUi.AUTH_DATA;
import static tests.TestBaseApiUi.authorizationApi;

public class LoginExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {

        UserLoginResponseModel userResponse = authorizationApi.login(AUTH_DATA);

        if (context.getTestInstance().isPresent()) {
            Object testInstance = context.getTestInstance().get();
            if (testInstance instanceof TestBaseApiUi) {
                TestBaseApiUi base = (TestBaseApiUi) testInstance;
                base.setUserResponse(userResponse);
            }
        }
    }
}
