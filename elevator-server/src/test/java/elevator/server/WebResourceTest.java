package elevator.server;

import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.fest.assertions.Assertions.assertThat;

public class WebResourceTest {

    @ClassRule
    public static ElevatorServerRule elevatorServerRule = new ElevatorServerRule();

    @Test
    public void should_initialize_maxNumberOfUsers_with_zero() {
        Response response = elevatorServerRule.target.path("/admin/maxNumberOfUsers").request().buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("0");
    }

    @Test
    public void should_increase_maxNumberOfUsers() {
        Response response = elevatorServerRule.target.path("/admin/increaseMaxNumberOfUsers").request().buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("1");
    }

}
