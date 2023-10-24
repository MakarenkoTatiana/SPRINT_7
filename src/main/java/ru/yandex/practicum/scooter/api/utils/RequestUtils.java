package ru.yandex.practicum.scooter.api.utils;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
public class RequestUtils {
    public static Response sendGetRequest(String api) {
        return given()
                .contentType(JSON)
                .get(api);
    }
    public static Response sendPostRequest(String api, Object body) {
        return given()
                .contentType(JSON)
                .body(body)
                .post(api);
    }

    public static Response sendDeleteRequest(String api, Integer id) {
        return given()
                .delete(api.replace("@id",id.toString()));
    }
}
