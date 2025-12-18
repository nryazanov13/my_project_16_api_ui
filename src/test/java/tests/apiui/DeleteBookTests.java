package tests.apiui;

import helpers.extensions.WithLogin;
import io.qameta.allure.*;
import models.BookModel;
import models.BooksRequestModel;
import models.UserAccountResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;
import tests.TestBase;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("BookStore")
@Feature("Удаление книг из профиля пользователя")
@Story("Я как пользователь хочу иметь возможность удалять книги из профиля пользователя")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api_ui")
public class DeleteBookTests extends TestBase {

    private static final String BOOK_ISBN = "9781449325862";
    private static final String BOOK_TITLE = "Git Pocket Guide";

    @Test
    @WithLogin
    @DisplayName("Проверка удаления книги из профиля пользователя через UI")
    void deleteBookFromProfileTest() {
        step("Авторизуемся и настраиваем куки", () -> {
            loginUser();
            setupAuthCookies();
        });

        step("Очищаем коллекцию книг через API", () -> {
            booksApi.deleteAllBooks(userResponse);
        });

        step("Добавляем книгу в коллекцию через API", () -> {
            BookModel book = new BookModel(BOOK_ISBN, userResponse.getUserId());
            BooksRequestModel booksList = new BooksRequestModel(userResponse.getUserId(), List.of(book));
            booksApi.addBooks(userResponse, booksList);
        });

        step("Удаляем книгу через UI", () -> {
            new ProfilePage()
                    .openPage()
                    .checkBookIsVisible(BOOK_TITLE)
                    .deleteBook()
                    .checkBookIsNotVisible(BOOK_TITLE);
        });

        step("Проверяем через API, что коллекция пуста", () -> {
            UserAccountResponseModel userAccount = userApi.getUserProfile(userResponse);
            assertTrue(userAccount.getBooks().isEmpty(),
                    "После удаления книги коллекция должна быть пустой");
        });
    }
}
