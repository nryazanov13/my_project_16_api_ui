package tests;

import helpers.extensions.WithLogin;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("new")
public class NewDemoQaTests extends TestBase {

    private static final String BOOK_ISBN = "9781449325862";
    private static final String BOOK_TITLE = "Git Pocket Guide";

    @Test
    @WithLogin
    @DisplayName("Проверка добавления книги в профиль, ТЕСТ НА УДАЛЕНИЕ СЛЕДУЮЩИЙ")
    void addOneBookToTheCollectionTest() {
        step("Авторизуемся и настраиваем куки", () -> {
            loginUser();
            setupAuthCookies();
        });

        step("Добавляем книгу в коллекцию через API", () -> {
            BookModel book = new BookModel(BOOK_ISBN);
            BooksRequestModel booksList = new BooksRequestModel(userResponse.getUserId(), List.of(book));
            booksApi.addBook(userResponse, booksList);
        });

        step("Проверяем отображение книги в профиле через UI", () -> {
            new ProfilePage()
                    .openPage()
                    .checkBookIsVisible(BOOK_TITLE);
        });
    }

    @Test
    //@WithLogin
    @DisplayName("Проверка удаления книги из профиля")
    void deleteBookFromProfileTest() {
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
            booksApi.addBook(userResponse, booksList);
        });

        step("Удаляем книгу через UI", () -> {
            new ProfilePage()
                    .openPage()
                    .checkBookIsVisible(BOOK_TITLE)
                    .deleteBook();
        });

        step("Проверяем через API, что коллекция пуста", () -> {
            BooksResponseModel booksResponse = booksApi.getAllBooksFromTheCollection(userResponse);
            assertTrue(booksResponse.getBooks().isEmpty(),
                    "После удаления книги коллекция должна быть пустой");
        });
    }
}