package ru.frigesty.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSingleUserModel {
    UserData data;
    UserSupport support;

    @Data
    public static class UserData {
        int id;
        @JsonProperty("first_name")
        String firstName;
        @JsonProperty("last_name")
        String lastName;
        String email, avatar;
    }
    @Data

    public static class UserSupport {
        String url, text;
    }
}