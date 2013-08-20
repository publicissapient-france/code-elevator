package elevator.server;

import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.*;
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
                .header(AUTHORIZATION, credentials("admin", elevatorServerRule.password()))
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("0");
    }

    @Test
    public void should_increase_maxNumberOfUsers() {
        Response response = elevatorServerRule.target
                .path("/admin/increaseMaxNumberOfUsers").request()
                .header(AUTHORIZATION, credentials("admin", elevatorServerRule.password()))
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("1");
    }

    @Test
    public void should_not_reset_with_unknow_user() {
        String password = null;
        try {
            password = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player@provider.com")
                    .queryParam("pseudo", "player")
                    .queryParam("serverURL", "http://localhost").request()
                    .buildPost(null).invoke().readEntity(String.class);

            Response response = elevatorServerRule.target
                    .path("/player/reset")
                    .queryParam("email", "unkown@provider.com").request()
                    .header(AUTHORIZATION, credentials("player@provider.com", password))
                    .buildPost(null).invoke();

            assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
        } finally {
            if (password != null) {
                elevatorServerRule.target.path("/player/unregister")
                        .queryParam("email", "player@provider.com").request()
                        .header(AUTHORIZATION, credentials("player@provider.com", password))
                        .buildPost(null).invoke();
            }
        }
    }

    @Test
    public void should_reset() {
        String password = null;
        try {
            password = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player@provider.com")
                    .queryParam("pseudo", "player")
                    .queryParam("serverURL", "http://localhost").request()
                    .buildPost(null).invoke().readEntity(String.class);

            Response response = elevatorServerRule.target
                    .path("/player/reset")
                    .queryParam("email", "player@provider.com").request()
                    .header(AUTHORIZATION, credentials("player@provider.com", password))
                    .buildPost(null).invoke();

            assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        } finally {
            if (password != null) {
                elevatorServerRule.target.path("/player/unregister")
                        .queryParam("email", "player@provider.com").request()
                        .header(AUTHORIZATION, credentials("player@provider.com", password))
                        .buildPost(null).invoke();
            }
        }
    }

    @Test
    public void should_unregister() {
        String password = elevatorServerRule.target.path("/player/register")
                .queryParam("email", "player@provider.com")
                .queryParam("pseudo", "player")
                .queryParam("serverURL", "http://localhost").request()
                .buildPost(null).invoke().readEntity(String.class);

        Response response = elevatorServerRule.target
                .path("/player/unregister")
                .queryParam("email", "player@provider.com").request()
                .header(AUTHORIZATION, credentials("player@provider.com", password))
                .buildPost(null).invoke();

        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    @Test
    public void should_resume() {
        String password = null;
        try {
            password = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player@provider.com")
                    .queryParam("pseudo", "player")
                    .queryParam("serverURL", "http://localhost").request()
                    .buildPost(null).invoke().readEntity(String.class);

            elevatorServerRule.target.path("/player/pause")
                    .queryParam("email", "player@provider.com").request()
                    .header(AUTHORIZATION, credentials("player@provider.com", password))
                    .buildPost(null).invoke();

            Response response = elevatorServerRule.target.path("/player/resume")
                    .queryParam("email", "player@provider.com").request()
                    .header(AUTHORIZATION, credentials("player@provider.com", password))
                    .buildPost(null).invoke();

            assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        } finally {
            if (password != null) {
                elevatorServerRule.target
                        .path("/player/unregister")
                        .queryParam("email", "player@provider.com").request()
                        .header(AUTHORIZATION, credentials("player@provider.com", password))
                        .buildPost(null).invoke();
            }
        }
    }

    private String credentials(String user, String password) {
        return "Basic " + printBase64Binary((user + ":" + password).getBytes());
    }

}
