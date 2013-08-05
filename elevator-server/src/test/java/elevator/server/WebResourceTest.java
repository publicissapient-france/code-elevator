package elevator.server;

import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.fest.assertions.Assertions.assertThat;

public class WebResourceTest {

    @ClassRule
    public static ElevatorServerRule elevatorServerRule = new ElevatorServerRule();

    @Test
    public void should_not_authorize_non_admin_request() {
        Response response = elevatorServerRule.target.path("/admin/maxNumberOfUsers").request().buildGet().invoke();
        assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void should_initialize_maxNumberOfUsers_with_zero() {
        Response response = elevatorServerRule.target
                .path("/admin/maxNumberOfUsers").request()
                .header(AUTHORIZATION, adminCredentials())
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("0");
    }

    @Test
    public void should_increase_maxNumberOfUsers() {
        Response response = elevatorServerRule.target
                .path("/admin/increaseMaxNumberOfUsers").request()
                .header(AUTHORIZATION, adminCredentials())
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("1");
    }

    private String adminCredentials() {
        return "Basic " + printBase64Binary(("admin:" + elevatorServerRule.password()).getBytes());
    }

}
