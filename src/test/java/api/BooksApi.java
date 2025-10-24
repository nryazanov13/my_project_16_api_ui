package api;

import models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;

public class BooksApi {
    public void deleteAllBooks(UserLoginResponseModel userResponseModel) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .queryParam("UserId", userResponseModel.getUserId())
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204));
    }

    public void addBook(UserLoginResponseModel userResponseModel, BooksRequestModel booksList) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(booksList)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201));
    }

    public UserAccountResponseModel getUserProfile(UserLoginResponseModel userResponseModel) {
        return given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .get("/Account/v1/User/{userId}", userResponseModel.getUserId()) // ✅ Правильный эндпоинт для профиля пользователя
                .then()
                .spec(responseSpec(200))  // ✅ Правильный статус для GET
                .extract()
                .as(UserAccountResponseModel.class);
    }

    public void deleteBook(UserLoginResponseModel userResponseModel, BookModel book) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(book)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(responseSpec(204));
    }
}
