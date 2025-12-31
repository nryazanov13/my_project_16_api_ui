package tests.apiui;

import helpers.extensions.WithLogin;
import io.qameta.allure.*;
import models.BookModel;
import models.BooksRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;
import tests.TestBaseApiUi;

import java.util.List;

import static io.qameta.allure.Allure.step;

@Epic("BookStore")
@Feature("Добавление книги в профиль пользователя")
@Story("Я как пользователь хочу иметь возможность добавлять книгу в профиль пользователя")
@Severity(SeverityLevel.BLOCKER)
@Owner("Nikita Ryazanov")
@Tag("api_ui")
public class AddBookTests extends TestBaseApiUi {

    private static final String BOOK_ISBN = "9781449325862";
    private static final String BOOK_TITLE = "Git Pocket Guide";

    @Test
    @WithLogin
    @DisplayName("Проверка добавленной книги в профиль пользователя через UI")
    void addOneBookToTheCollectionTest() {
        step("Авторизуемся и настраиваем куки", () -> {
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

        step("Проверяем отображение книги в профиле через UI", () -> {
            new ProfilePage()
                    .openPage()
                    .checkBookIsVisible(BOOK_TITLE);
        });
    }
}
