package tests.api;

import io.qameta.allure.*;
import models.BookModel;
import models.BooksRequestModel;
import models.UserAccountResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("BookStore")
@Feature("Удаление книги из профиля пользователя")
@Story("Я как пользователь хочу иметь возможность удалять книгу из профиля пользователя")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class DeleteBookTests extends TestBase {
    private static final String BOOK_ISBN = "9781449325862";

    @Test
    @DisplayName("Проверка удаления книги из профиля пользователя через API")
    void deleteBookFromProfileTest() {

        step("Авторизуемся и настраиваем куки", () -> {
            loginUser();
            setupAuthCookies();
        });

        step("Очищаем коллекцию книг через API", () -> {
            booksApi.deleteAllBooks(userResponse);
        });

        step("Добавляем книгу в коллекцию через API", () -> {
            book = new BookModel(BOOK_ISBN, userResponse.getUserId());
            BooksRequestModel booksList = new BooksRequestModel(userResponse.getUserId(), List.of(book));
            booksApi.addBooks(userResponse, booksList);
        });

        step("Удаляем книгу через API", () -> {
            booksApi.deleteBook(userResponse, book);
        });

        step("Проверяем через API, что коллекция пуста", () -> {
            UserAccountResponseModel userAccount = userApi.getUserProfile(userResponse);
            assertTrue(userAccount.getBooks().isEmpty(),
                    "После удаления книги - коллекция должна быть пустой");
        });
    }
}
