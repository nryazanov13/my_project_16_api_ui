package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    // Локаторы элементов
    private final SelenideElement booksTable = $(".ReactTable");
    private final SelenideElement profileHeader = $(".main-header");
    private final SelenideElement userNameValue = $("#userName-value");
    private final SelenideElement deleteButton = $(".delete-record-undefined");

    // Методы для взаимодействия со страницей
    public ProfilePage openPage() {
        open("/profile");
        return this;
    }

    public ProfilePage checkBookIsVisible(String book) {
        booksTable.$(byText(book)).shouldBe(visible);
        return this;
    }

    public ProfilePage deleteBook() {
        deleteButton.click();
        return this;
    }

}