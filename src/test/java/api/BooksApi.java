package api;

import models.BookModel;
import models.BooksRequestModel;
import models.BooksResponseModel;
import models.UserResponseModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class BooksApi {
    public void deleteAllBooks(UserResponseModel userResponseModel) {
        given()
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .queryParam("UserId", userResponseModel.getUserId())
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .statusCode(204);
    }

    public void addBook(UserResponseModel userResponseModel, BooksRequestModel booksList) {
        given()
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(booksList)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .statusCode(201);
    }

    public BooksResponseModel getAllBooksFromTheCollection(UserResponseModel userResponseModel) {
        return given()
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .statusCode(200)
                .extract()
                .as(BooksResponseModel.class);
    }

    public void deleteBook(UserResponseModel userResponseModel, BookModel book) {
        given()
                .contentType(JSON)
                .header("Authorization", "Bearer " + userResponseModel.getToken())
                .body(book)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .statusCode(204);
    }

}
