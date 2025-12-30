package tests;

import api.AccountApi;
import api.AuthorizationApi;
import api.BooksApi;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.CredentialsConfig;
import helpers.WebTestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import models.UserAccountRequestModel;
import models.UserLoginResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static utils.RandomUtils.getRandomFirstName;

public class TestBaseApi {
    static CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);
    static WebTestConfig webTestConfig = ConfigFactory.create(WebTestConfig.class);

    public static AuthorizationApi authorizationApi = new AuthorizationApi();
    protected BooksApi booksApi = new BooksApi();
    protected AccountApi userApi = new AccountApi();

    protected static final String USERNAME = credentialsConfig.demoqaUserLogin();
    protected static final String CORRECT_PASSWORD = credentialsConfig.demoqaCorrectUserPassword();
    protected static final String INCORRECT_PASSWORD = credentialsConfig.demoqaIncorrectUserPassword();

    private UserLoginResponseModel cachedStaticUserResponse;
    private String currentRandomUsername;

    public UserLoginResponseModel getUserStaticCorrectResponse() {
        if (cachedStaticUserResponse == null) {
            UserAccountRequestModel authData = getStaticCorrectAuthData();
            cachedStaticUserResponse = authorizationApi.login(authData);
            validateAuthResponse(cachedStaticUserResponse, "статического пользователя");
        }
        return cachedStaticUserResponse;
    }

    public String getRandomUsername() {
        if (currentRandomUsername == null) {
            currentRandomUsername = getRandomFirstName();
        }
        return currentRandomUsername;
    }

    public UserAccountRequestModel getStaticCorrectAuthData() {
        return new UserAccountRequestModel(USERNAME, CORRECT_PASSWORD);
    }

    public UserAccountRequestModel getIncorrectAuthData() {
        return new UserAccountRequestModel(getRandomUsername(), INCORRECT_PASSWORD);
    }

    private void validateAuthResponse(UserLoginResponseModel response, String userType) {
        if (response == null) {
            throw new RuntimeException("Ответ авторизации для " + userType + " равен null");
        }
        if (response.getToken() == null || response.getToken().isEmpty()) {
            throw new RuntimeException("Токен для " + userType + " не получен");
        }
        if (response.getUserId() == null || response.getUserId().isEmpty()) {
            throw new RuntimeException("UserID для " + userType + " не получен");
        }
    }

    @BeforeAll
    static void setAll() {
        Configuration.browser = getProperty("browser", webTestConfig.browserName());
        Configuration.browserVersion = getProperty("browserVersion", webTestConfig.browserVersion());
        Configuration.browserSize = getProperty("browserSize", webTestConfig.browserSize());

        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";
        RestAssured.defaultParser = Parser.JSON;

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

    private static String getProperty(String name, String defaultValue) {
        String property = System.getProperty(name);
        return (property != null && !property.isEmpty()) ? property : defaultValue;
    }
}