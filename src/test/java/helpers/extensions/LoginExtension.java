package helpers.extensions;

import api.AuthorizationApi;
import lombok.Getter;
import models.UserLoginModel;
import models.UserResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LoginExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        AuthorizationApi authorizationApi = new AuthorizationApi();
        UserLoginModel user = new UserLoginModel("test111", "Test1234567!");

        UserResponseModel userResponse = authorizationApi.login(user);
        authorizationApi.setAuthCookies(userResponse);
    }
}
