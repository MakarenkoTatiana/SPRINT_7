import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CreateCourierRequest;
import ru.yandex.practicum.scooter.api.model.CourierLoginRequest;

import static org.junit.Assert.*;
import static ru.yandex.practicum.scooter.api.config.Config.*;
import static ru.yandex.practicum.scooter.api.cridentials.CourierCridentials.*;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.sendDeleteRequest;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.sendPostRequest;
public class CouriersLoginTests {
    CreateCourierRequest createCourier = new CreateCourierRequest(DEFAULT_COURIER_LOGIN,DEFAULT_COURIER_PASSWORD,DEFAULT_COURIER_FIRST_NAME);
    CourierLoginRequest loginCourier = new CourierLoginRequest(DEFAULT_COURIER_LOGIN, DEFAULT_COURIER_PASSWORD);
    int createdCourierID;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        // Создание курьера для тестов на авторизацию
        Boolean createResult = sendPostRequest(CREATE_COURIER_API, createCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("ok");
        assertTrue(createResult);

        // Получение ID созданного курьера для очистки после тестов
        CourierLoginRequest createdCourier = new CourierLoginRequest(DEFAULT_COURIER_LOGIN, DEFAULT_COURIER_PASSWORD);
        createdCourierID = sendPostRequest(COURIER_LOGIN_API, createdCourier)
                .then()
                .extract()
                .path("id");
    }
    @After
    public void deleteCreatedCourier() {
        Boolean deleteResult = sendDeleteRequest(COURIER_DELETE_API, createdCourierID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("ok");
        assertTrue(deleteResult);
    }
    @Test
    public void courierLoginTest() {
        Response loginCourierResponse = sendPostRequest(COURIER_LOGIN_API, loginCourier);

        loginCourierResponse.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        assertNotNull(createdCourierID);
    }
    @Test
    public void courierLoginEmptyPasswordTest() {
        CourierLoginRequest loginCourierEmptyPassword = new CourierLoginRequest(DEFAULT_COURIER_LOGIN, "");
        Response loginCourierResponse = sendPostRequest(COURIER_LOGIN_API, loginCourierEmptyPassword);

        String actualMessage = loginCourierResponse.then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .path("message");
        String expectedMessage = "Недостаточно данных для входа";
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    public void courierLoginNotExistingLoginTest() {
        CourierLoginRequest loginNotExistingLogin = new CourierLoginRequest(NOT_EXISTING_COURIER_LOGIN, DEFAULT_COURIER_PASSWORD);
        Response loginCourierResponse = sendPostRequest(COURIER_LOGIN_API, loginNotExistingLogin);

        String actualMessage = loginCourierResponse.then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .path("message");
        String expectedMessage = "Учетная запись не найдена";
        assertEquals(expectedMessage, actualMessage);
    }
}
