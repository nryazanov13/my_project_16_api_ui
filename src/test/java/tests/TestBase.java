package tests;

import api.AuthorizationApi;
import api.BooksApi;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import models.UserLoginModel;
import models.UserResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    protected AuthorizationApi authorizationApi = new AuthorizationApi();
    protected BooksApi booksApi = new BooksApi();

    protected static final String USERNAME = "test111";
    protected static final String PASSWORD = "Test1234567!";

    protected UserResponseModel userResponse;

    @BeforeAll
    static void setAll() {
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com/";
    }

    protected UserResponseModel loginUser() {
        userResponse = authorizationApi.login(new UserLoginModel(USERNAME, PASSWORD));
        return userResponse;
    }

    protected void setupAuthCookies() {
        authorizationApi.setAuthCookies(userResponse);
    }

    @AfterEach
    void setUp() {
        closeWebDriver();
    }
}