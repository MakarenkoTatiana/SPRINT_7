import io.restassured.RestAssured;
import static org.hamcrest.Matchers.notNullValue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static ru.yandex.practicum.scooter.api.config.Config.BASE_URL;
import static ru.yandex.practicum.scooter.api.config.Config.ORDERS_API;
import static ru.yandex.practicum.scooter.api.utils.RequestUtils.sendGetRequest;
public class OrderListTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void getOrderListTest() {
        List<Integer> orders = sendGetRequest(ORDERS_API)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("orders.id");
        assertFalse(orders.isEmpty());
    }
}
