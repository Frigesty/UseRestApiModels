package ru.frigesty.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetListResourceModel {
    @JsonProperty("per_page")
    int perPage;
    @JsonProperty("total_pages")
    int totalPages;
    int page, total;

    GetListUsersModel.Support support;
    List<GetListResourceModel.DataInfo> data;

    @Data
    public static class DataInfo {
        int id, year;
        @JsonProperty("pantone_value")
        String pantoneValue;
        String color, name;
    }

    @Data
    public static class Support {
        String url, text;
    }
}
