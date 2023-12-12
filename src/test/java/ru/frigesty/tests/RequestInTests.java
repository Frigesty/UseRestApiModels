package ru.frigesty.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.frigesty.models.*;
import java.util.HashSet;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.frigesty.specs.LoginSpecs.*;
@DisplayName("API тесты")
public class RequestInTests extends TestBase {

    @Test
    @DisplayName("Проверка коректности данных в List Users")
    void correctDataInPageListUsersTest() {

        Integer[] userId = {7, 8, 9, 10, 11, 12};

        GetListUsersModel response = step("Делаем запрос", () ->
                given()
                        .spec(loginRequestSpecBase)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .body(matchesJsonSchemaInClasspath("schemes/listUsersScheme.json"))
                        .spec(loginResponseSpec)
                        .extract().as(GetListUsersModel.class));

        HashSet<Integer> idFromResponse = new HashSet<>();

        List<GetListUsersModel.DataInfo> usersFromResponse = response.getData();

        for (GetListUsersModel.DataInfo user : usersFromResponse) {
            idFromResponse.add(user.getId());
        }

        step("Сверяем и проверяем количество id", () -> {
            for (int id : userId) {
                assertTrue(idFromResponse.contains(id));
            }
            assertEquals(idFromResponse.size(), userId.length);
        });

        step("Проверяем данные из ответа", () -> {
            assertEquals(2, response.getPage());
            assertEquals(6, response.getPerPage());
            assertEquals(12, response.getTotal());
            assertEquals(2, response.getTotalPages());
        });

        step("Проверяем данные во втором объекте из массива data", () -> {
            List<GetListUsersModel.DataInfo> data = response.getData();
            assertEquals(8, data.get(1).getId());
            assertEquals("lindsay.ferguson@reqres.in", data.get(1).getEmail());
            assertEquals("Lindsay", data.get(1).getFirstName());
            assertEquals("Ferguson", data.get(1).getLastName());
            assertEquals("https://reqres.in/img/faces/8-image.jpg", data.get(1).getAvatar());
        });
    }

    @Test
    @DisplayName("Проверка коректности данных на станице с Single User")
    void correctDataInPageSingleUserTest() {

        GetSingleUserModel response = step("Делаем запрос", () ->
                given()
                        .spec(loginRequestSpecBase)
                        .when()
                        .get("/users/2")
                        .then()
                        .body(matchesJsonSchemaInClasspath("schemes/singleUserScheme.json"))
                        .spec(loginResponseSpec)
                        .extract().as(GetSingleUserModel.class));
        step("Проверяем данные в объекте data", () -> {
            assertEquals(2, response.getData().getId());
            assertEquals("janet.weaver@reqres.in", response.getData().getEmail());
            assertEquals("Janet", response.getData().getFirstName());
            assertEquals("Weaver", response.getData().getLastName());
            assertEquals("https://reqres.in/img/faces/2-image.jpg", response.getData().getAvatar());
        });
    }

    @Test
    @DisplayName("Проверка кода 404")
    void pageSingleUserNotFoundTest() {
        step("Проверяем что запрос выдает код 404", () -> {
            given()
                    .spec(loginRequestSpecBase)
                    .when()
                    .get("/users/23")
                    .then()
                    .spec(notFoundSpec);
        });
    }

    @DisplayName("Проверка коректности данных в List Resource")
    @Test
    void correctDataInPageListResourceTest() {
        Integer[] userId = {1, 2, 3, 4, 5, 6};

        GetListResourceModel response = step("Делаем запрос", () ->
                given()
                        .spec(loginRequestSpecBase)
                        .when()
                        .get("/unknown")
                        .then()
                        .body(matchesJsonSchemaInClasspath("schemes/listResourceScheme.json"))
                        .spec(loginResponseSpec)
                        .extract().as(GetListResourceModel.class));

        HashSet<Integer> idFromResponse = new HashSet<>();

        List<GetListResourceModel.DataInfo> usersFromResponse = response.getData();

        for (GetListResourceModel.DataInfo user : usersFromResponse) {
            idFromResponse.add(user.getId());
        }

        step("Сверяем и проверяем количество id", () -> {
            for (int id : userId) {
                assertTrue(idFromResponse.contains(id));
            }
            assertEquals(idFromResponse.size(), userId.length);
        });

        step("Проверяем данные из ответа", () -> {
            assertEquals(1, response.getPage());
            assertEquals(6, response.getPerPage());
            assertEquals(12, response.getTotal());
            assertEquals(2, response.getTotalPages());
        });

        step("Проверяем данные в третьем объекте из массива data", () -> {
            List<GetListResourceModel.DataInfo> data = response.getData();
            assertEquals(3, data.get(2).getId());
            assertEquals("true red", data.get(2).getName());
            assertEquals(2002, data.get(2).getYear());
            assertEquals("#BF1932", data.get(2).getColor());
            assertEquals("19-1664", data.get(2).getPantoneValue());
        });
    }

    @Test
    @DisplayName("Проверка успешной авторизации")
    void AuthTest() {
        LoginRequestModel loginRequest = new LoginRequestModel();
        loginRequest.setEmail("eve.holt@reqres.in");
        loginRequest.setPassword("pistol");
        LoginResponseModel response = step("Вводим данные", () ->
                given(loginRequestSpecBase)
                        .body(loginRequest)
                        .when()
                        .post("/login")
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class)
        );

        step("Проверяем успешость авторизации", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }
}