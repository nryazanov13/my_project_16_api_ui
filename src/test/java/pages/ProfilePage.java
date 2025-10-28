package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    private final SelenideElement booksTable = $(".ReactTable");
    private final SelenideElement deleteButton = $("#delete-record-undefined");
    private final SelenideElement okButton = $("#closeSmallModal-ok");

    // Методы для взаимодействия со страницей
    public ProfilePage openPage() {
        open("/profile");
        return this;
    }

    public ProfilePage checkBookIsVisible(String book) {
        booksTable.$(byText(book)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkBookIsNotVisible(String book) {
        booksTable.$(byText(book)).shouldNotBe(visible);
        return this;
    }

    public ProfilePage deleteBook() {
        deleteButton.click();
        okButton.click();
        return this;
    }

}