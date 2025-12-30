package tests.api;

import helpers.extensions.WithLogin;
import io.qameta.allure.*;
import models.BookModel;
import models.BooksRequestModel;
import models.UserAccountResponseModel;
import models.UserLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("BookStore")
@Feature("Удаление книги из профиля пользователя")
@Story("Я как пользователь хочу иметь возможность удалять книгу из профиля пользователя")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api")
public class DeleteBookTests extends TestBaseApi {
    private static final String BOOK_ISBN = "9781449325862";

    @Test
    @WithLogin
    @DisplayName("Проверка удаления книги из профиля пользователя через API")
    void deleteBookFromProfileTest() {

        UserLoginResponseModel user = getUserStaticCorrectResponse();
        BookModel book = new BookModel(BOOK_ISBN, user.getUserId());

        step("Очищаем коллекцию книг через API", () -> {
            booksApi.deleteAllBooks(user);
        });

        step("Добавляем книгу в коллекцию через API", () -> {
            BooksRequestModel booksList = new BooksRequestModel(user.getUserId(), List.of(book));
            booksApi.addBooks(user, booksList);
        });

        step("Удаляем книгу через API", () -> {
            booksApi.deleteBook(user, book);
        });

        step("Проверяем через API, что коллекция пуста", () -> {
            UserAccountResponseModel userAccount = userApi.getUserProfile(user);
            assertTrue(userAccount.getBooks().isEmpty(),
                    "После удаления книги - коллекция должна быть пустой");
        });
    }
}