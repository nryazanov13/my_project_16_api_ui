package models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookModel {
    private String isbn;
    private String userId;

    public BookModel(String isbn) {
        this.isbn = isbn;
    }

    public BookModel(String isbn, String userId) {
        this.isbn = isbn;
        this.userId = userId;
    }
}
