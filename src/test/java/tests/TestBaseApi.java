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
import lombok.Setter;
import models.BookModel;
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
    protected BookModel book;

    protected static final String USERNAME = credentialsConfig.demoqaUserLogin();
    protected static final String RANDOM_USERNAME = getRandomFirstName();
    protected static final String CORRECT_PASSWORD = credentialsConfig.demoqaCorrectUserPassword();
    protected static final String INCORRECT_PASSWORD = credentialsConfig.demoqaIncorrectUserPassword();


    public static final UserAccountRequestModel CORRECT_RANDOM_USERNAME_AUTH_DATA =
            new UserAccountRequestModel(RANDOM_USERNAME, CORRECT_PASSWORD);

    public static final UserAccountRequestModel STATIC_CORRECT_AUTH_DATA =
            new UserAccountRequestModel(USERNAME, CORRECT_PASSWORD);

    public static final UserAccountRequestModel INCORRECT_AUTH_DATA =
            new UserAccountRequestModel(RANDOM_USERNAME, INCORRECT_PASSWORD);

    @Setter
    protected UserLoginResponseModel userStaticCorrectResponse =
            authorizationApi.login(STATIC_CORRECT_AUTH_DATA);

    @Setter
    protected UserLoginResponseModel userRandomResponse =
            authorizationApi.login(CORRECT_RANDOM_USERNAME_AUTH_DATA);

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
