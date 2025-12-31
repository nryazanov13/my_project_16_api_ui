package models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponseModel {

    @JsonProperty("userId")
    @JsonAlias({"userID", "userId"})
    private String userId;
    private String username;
    private List<BookDetails> books;

}
