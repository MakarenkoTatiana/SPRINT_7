package ru.yandex.practicum.scooter.api.utils;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.qameta.allure.Step; // импорт Step

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class RequestUtils {
    @Step("Получение списка заказов")
    public static Response sendGetRequest(String api) {
        return given()
                .contentType(JSON)
                .get(api);
    }

    @Step("Создание курьера/заказа")
    public static Response sendPostRequest(String api, Object body) {
        return given()
                .contentType(JSON)
                .body(body)
                .post(api);
    }

    @Step("Удаление, создаваемого для тестов курьера")
    public static Response sendDeleteRequest(String api, Integer id) {
        return given()
                .delete(api.replace("@id", id.toString()));
    }
}
