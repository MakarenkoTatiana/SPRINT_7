import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CreateCourierRequest;
import ru.yandex.practicum.scooter.api.model.CourierLoginRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.scooter.api.config.Config.*;
import static ru.yandex.practicum.scooter.api.cridentials.CourierCridentials.*;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.*;
public class CreateCourierTests {

    CreateCourierRequest createCourier = new CreateCourierRequest(DEFAULT_COURIER_LOGIN,DEFAULT_COURIER_PASSWORD,DEFAULT_COURIER_FIRST_NAME);
    private Boolean clearCreatedCourier;
    @Before
    public void setUp() {
        clearCreatedCourier = true;
        RestAssured.baseURI = BASE_URL;
    }
    @After
    public void deleteCreatedCourier() {
        // Очистка создаваемого для тестов курьера
        // ID созданного курьера можно получить только путем логина, поэтому приходится предварительно выполнять его
        // Переменная clearCreatedCourier добавлена потому что не во всех тестах происходит добавление курьера
        if (clearCreatedCourier) {
            CourierLoginRequest createdCourier = new CourierLoginRequest(DEFAULT_COURIER_LOGIN, DEFAULT_COURIER_PASSWORD);
            int createdCourierID = sendPostRequest(COURIER_LOGIN_API, createdCourier)
                    .then()
                    .extract()
                    .path("id");
            Boolean deleteResult = sendDeleteRequest(COURIER_DELETE_API, createdCourierID)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .path("ok");
            assertTrue(deleteResult);
        }
    }
    @Test
    public void createCourierTest() {
        Boolean createResult = sendPostRequest(CREATE_COURIER_API, createCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("ok");
        assertTrue(createResult);
    }

    @Test
    public void createExistingCourierTest() {
        Boolean createResult = sendPostRequest(CREATE_COURIER_API, createCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("ok");
        assertTrue(createResult);

        String actualMessage = sendPostRequest(CREATE_COURIER_API, createCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .extract()
                .path("message");
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createCourierEmptyPasswordTest() {
        clearCreatedCourier = false;
        CreateCourierRequest createCourierEmptyPassword = new CreateCourierRequest(DEFAULT_COURIER_LOGIN,"",DEFAULT_COURIER_FIRST_NAME);
        String actualMessage = sendPostRequest(CREATE_COURIER_API, createCourierEmptyPassword)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .path("message");
        String expectedMessage = "Недостаточно данных для создания учетной записи";
        assertEquals(expectedMessage, actualMessage);
    }
}
