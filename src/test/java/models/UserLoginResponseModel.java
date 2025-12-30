package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseModel {
    String userId;
    String username;
    String password;
    String token;
    String expires;
    @JsonProperty("created_date")
    String createdDate;
    Boolean isActive;
}