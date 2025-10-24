package tests;

import models.BookModel;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.Cookie;
import io.restassured.response.Response;
import models.UserModel;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;

@Tag("old")
public class OldDemoQaTests extends TestBase {

    UserModel user = new UserModel("test111", "Test1234567!");
    BookModel gitPocketGuideIsbn = new BookModel("9781449325862");
    //String userId = authResponse.path("userId");
    //UserCollection users = new UserCollection(userId, List.of(gitPocketGuideIsbn));

    @Test
    void successfulLoginWithUiTest() {
        open("/login");
        $("#username").setValue(user.getUserName());
        $("#password").setValue(user.getPassword()).pressEnter();
    }

    @Test
    void successfulLoginWithApiTest() {
        Response authResponse = step("Login with a user", () ->
                given(baseRequestSpec)
                        .body(user)
                        .when()
                        .post("/Account/v1/login")
                        .then()
                        .spec(responseSpec(200))
                        .extract().response());

        step("Open browser and check", () -> {
            open("/favicon.ico");

            getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

            open("/profile");
            $("#userName-value").shouldHave(text(user.getUserName()));
        });
    }

    @Test
    void addBookToCollectionTest() {
        Response authResponse = step("Login with a user", () ->
                given(baseRequestSpec)
                        .body(user)
                        .when()
                        .post("/Account/v1/login")
                        .then()
                        .spec(responseSpec(200))
                        .extract().response());

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204));

        String isbn = "9781449325862";
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                authResponse.path("userId"), isbn);

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201));

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");
        $(".ReactTable").shouldHave(text("Speaking JavaScript"));
    }

    //необходимо сделать первые шаги через апи и затем сделать удаление через иконку и затем сделать проверку в списке , что книг у нас нет
    @Test
    void deleteOneBookFromTheCollection() {

        //authorize

        Response authResponse = step("Login with a user", () ->
                given(baseRequestSpec)
                        .body(user)
                        .when()
                        .post("/Account/v1/login")
                        .then()
                        .spec(responseSpec(200))
                        .extract().response());

        //clear the collection

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204));


        //add book

        String isbn = "9781449325862";
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                authResponse.path("userId"), isbn);

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201));

        BookModel deletedBook = new BookModel(isbn, authResponse.path("userId"));

        //delete book

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(deletedBook)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(responseSpec(204));
    }
}
