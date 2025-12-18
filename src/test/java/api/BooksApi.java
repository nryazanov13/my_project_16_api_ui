package api;

import io.qameta.allure.Step;
import models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.RequestSpecs.baseRequestSpec;
import static specs.ResponseSpecs.responseSpec;

public class BooksApi {

    private static final String BOOKS = "/BookStore/v1/Books";
    private static final String BOOK = "/BookStore/v1/Book";


    @Step("Удаление Всех книг у пользователя")
    public void deleteAllBooks(UserLoginResponseModel userResponseModel) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .queryParam("UserId", userResponseModel.getUserId())
                .when()
                .delete(BOOKS)
                .then()
                .spec(responseSpec(204));
    }

    @Step("Удаление Одной книги у пользователя")
    public void deleteBook(UserLoginResponseModel userResponseModel, BookModel book) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(book)
                .when()
                .delete(BOOK)
                .then()
                .spec(responseSpec(204));
    }

    public void addBooks(UserLoginResponseModel userResponseModel, BooksRequestModel booksList) {
        given(baseRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(booksList)
                .when()
                .post(BOOKS)
                .then()
                .spec(responseSpec(201));
    }

    @Step("Получения книги по ISBN GET " + BOOK + ", Возвращаем response")
    public BookDetails getBookWithResponse(String isbn) {
        return given(baseRequestSpec)
                .queryParam("ISBN", isbn)
                .when()
                .get(BOOK)
                .then()
                .spec(responseSpec(200))
                .extract().as(BookDetails.class);
    }
}
