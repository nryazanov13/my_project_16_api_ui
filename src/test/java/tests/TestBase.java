package tests;

import api.AuthorizationApi;
import api.BooksApi;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import helpers.CredentialsConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import lombok.Setter;
import models.UserModel;
import models.UserLoginResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {

    static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);

    public static AuthorizationApi authorizationApi = new AuthorizationApi(); // ← ДОБАВЬТЕ static
    protected BooksApi booksApi = new BooksApi();

    protected static final String USERNAME = "test111";
    protected static final String PASSWORD = "Test1234567!";

    @Setter
    protected UserLoginResponseModel userResponse;

    @Setter
    protected UserModel user;

    protected void loginUser() {
        userResponse = authorizationApi.login(new UserModel(USERNAME, PASSWORD));
    }

    protected void setupAuthCookies() {
        authorizationApi.setAuthCookies(userResponse);
    }

    @BeforeAll
    static void setAll() {
        Configuration.browser = getProperty("browser", "chrome");
        Configuration.browserSize = getProperty("browserSize", "1920x1080");

        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";

        Configuration.pageLoadStrategy = "eager";

        String remoteHost = System.getProperty("remoteHost");
        if (remoteHost != null && !remoteHost.isEmpty()) {
            String login = config.login();
            String password = config.password();
            Configuration.remote = String.format("https://%s:%s@%s/wd/hub", login, password, remoteHost);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }

    @BeforeEach
    void addAllureListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        Selenide.closeWebDriver();
    }

    // Вспомогательный метод для получения свойств с дефолтными значениями
    private static String getProperty(String name, String defaultValue) {
        String property = System.getProperty(name);
        return (property != null && !property.isEmpty()) ? property : defaultValue;
    }
}