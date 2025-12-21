package tests;

import api.AuthorizationApi;
import api.BooksApi;
import api.AccountApi;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import helpers.CredentialsConfig;
import helpers.WebTestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import lombok.Setter;
import models.BookModel;
import models.UserModel;
import models.UserLoginResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBaseApiUi {

    static CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);
    static WebTestConfig webTestConfig = ConfigFactory.create(WebTestConfig.class);

    public static AuthorizationApi authorizationApi = new AuthorizationApi();
    protected BooksApi booksApi = new BooksApi();
    protected AccountApi userApi = new AccountApi();

    protected static final String USERNAME = credentialsConfig.demoqaUserLogin();
    protected static final String PASSWORD = credentialsConfig.demoqaUserPassword();

    public static final UserModel AUTH_DATA = new UserModel(USERNAME, PASSWORD);

    @Setter
    protected UserLoginResponseModel userResponse = authorizationApi.login(AUTH_DATA);

    protected void setupAuthCookies() {
        authorizationApi.setAuthCookies(userResponse);
    }

    @BeforeAll
    static void setAll() {
        // ГИБРИДНЫЙ ПОДХОД: системные переменные имеют приоритет над конфигом
        Configuration.browser = getProperty("browser", webTestConfig.browserName());
        Configuration.browserVersion = getProperty("browserVersion", webTestConfig.browserVersion());
        Configuration.browserSize = getProperty("browserSize", webTestConfig.browserSize());

        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";

        Configuration.pageLoadStrategy = "eager";
        Configuration.pageLoadTimeout = webTestConfig.pageLoadTimeout() * 1000L;

        if (webTestConfig.isRemote()) {
            setupRemoteDriver();
        } else {
            System.out.println("💻 Локальный запуск");
        }
    }

    private static void setupRemoteDriver() {
        String login = credentialsConfig.selenoidLogin();
        String password = credentialsConfig.selenoidPassword();
        String remoteHost = webTestConfig.remoteHost();

        Configuration.remote = String.format("https://%s:%s@%s/wd/hub", login, password, remoteHost);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", webTestConfig.enableVnc(),
                "enableVideo", webTestConfig.enableVideo()
        ));
        Configuration.browserCapabilities = capabilities;

        System.out.println("🚀 Удаленный запуск на: " + remoteHost);
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

        // ✅ Видео только для удаленного запуска
        if ("remote".equals(System.getProperty("env")) && webTestConfig.enableVideo()) {
            Attach.addVideo();
        }

        Selenide.closeWebDriver();
    }

    private static String getProperty(String name, String defaultValue) {
        String property = System.getProperty(name);
        return (property != null && !property.isEmpty()) ? property : defaultValue;
    }
}