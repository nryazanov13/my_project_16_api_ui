package tests.api;

import helpers.extensions.WithLogin;
import io.qameta.allure.*;
import models.BookDetails;
import models.BookModel;
import models.BooksRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("BookStore")
@Feature("Добавление книги в профиль пользователя")
@Story("Я как пользователь хочу иметь возможность добавлять книгу в профиль пользователя")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class PostBookTest extends TestBase {

    private static final String BOOK_ISBN = "9781449325862";
    private static final String BOOK_TITLE = "Git Pocket Guide";

    @Test
    @WithLogin
    @DisplayName("Проверка добавленной книги в профиль пользователя через API")
    void addOneBookToTheCollectionTest() {
        step("Авторизуемся и настраиваем куки", () -> {
            loginUser();
            setupAuthCookies();
        });

        step("Очищаем коллекцию книг через API", () -> {
            booksApi.deleteAllBooks(userResponse);
        });

        step("Добавляем книгу в коллекцию через API", () -> {
            BookModel book = new BookModel(BOOK_ISBN);
            BooksRequestModel booksList = new BooksRequestModel(userResponse.getUserId(), List.of(book));
            booksApi.addBooks(userResponse, booksList);
        });

        step("Проверить ISBN книги и название", () -> {
            BookDetails book = booksApi.getBookWithResponse(BOOK_ISBN);
            assertEquals(BOOK_ISBN, book.getIsbn(),
                    "Ожидаемое значение isbn " + BOOK_ISBN + "соответствует фактическому " + book.getIsbn());
            assertEquals(BOOK_TITLE, book.getTitle(),
                    "Ожидаемое значение названия " + BOOK_TITLE + "соответствует фактическому " + book.getTitle());
        });
    }
}
