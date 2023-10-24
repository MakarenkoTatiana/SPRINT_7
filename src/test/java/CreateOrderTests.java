import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.model.CreateCourierRequest;
import ru.yandex.practicum.scooter.api.model.CreateOrderRequest;
import ru.yandex.practicum.scooter.api.model.CourierLoginRequest;

import java.util.List;

import static org.junit.Assert.*;
import static ru.yandex.practicum.scooter.api.config.Config.*;
import static ru.yandex.practicum.scooter.api.cridentials.CourierCridentials.*;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.sendDeleteRequest;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.sendPostRequest;
@RunWith(Parameterized.class)
public class CreateOrderTests {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    CreateOrderRequest CreateOrder = new CreateOrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    public CreateOrderTests(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] getTestValues(){
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha",List.of("BLACK")},
                {"Tom", "Cat", "Texas, 1 st.", 4, "+7 800 800 800 80", 1, "2020-12-06", "",List.of("BLACK", "GREY")},
                {"Jerry", "Mouse", "Texas, 1 st.", 4, "+7 905 655 00 00", 1, "2020-07-19", "",List.of()},
        };
    }
    @Test
    public void createOrderTest() {
        int createdTrack = sendPostRequest(ORDERS_API, CreateOrder)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("track");
        assertNotNull(createdTrack);
    }
}
