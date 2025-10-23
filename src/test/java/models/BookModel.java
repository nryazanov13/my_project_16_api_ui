package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {
    private String isbn;
    private String userId;

    public BookModel(String isbn) {
        this.isbn = isbn;
    }
}
