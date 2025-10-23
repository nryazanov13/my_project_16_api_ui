package tests;

import api.AuthorizationApi;
import api.BooksApi;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import models.UserLoginModel;
import models.UserResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {

    protected AuthorizationApi authorizationApi = new AuthorizationApi();
    protected BooksApi booksApi = new BooksApi();

    protected static final String USERNAME = "test111";
    protected static final String PASSWORD = "Test1234567!";

    protected UserResponseModel userResponse;

    protected UserResponseModel loginUser() {
        userResponse = authorizationApi.login(new UserLoginModel(USERNAME, PASSWORD));
        return userResponse;
    }

    protected void setupAuthCookies() {
        authorizationApi.setAuthCookies(userResponse);
    }

    @BeforeAll
    static void setAll() {
        Configuration.baseUrl = System.getProperty("baseUrl", "https://demoqa.com");
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "128");
        Configuration.remote = System.getProperty("remoteUrl");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
        RestAssured.baseURI = System.getProperty("baseUrl", "https://demoqa.com");
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

}