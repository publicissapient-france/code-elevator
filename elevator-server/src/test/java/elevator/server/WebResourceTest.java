package elevator.server;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URISyntaxException;
import java.util.regex.Matcher;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.fest.assertions.Assertions.assertThat;

public class WebResourceTest {

    @Rule
    public ElevatorServerRule elevatorServerRule = new ElevatorServerRule();

    @Test
    public void should_not_authorize_non_admin_request() {
        Response response = elevatorServerRule.target.path("/admin/maxNumberOfUsers").request().buildGet().invoke();
        assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void should_initialize_maxNumberOfUsers_with_three() {
        Response response = elevatorServerRule.target
                .path("/admin/maxNumberOfUsers").request()
                .header(AUTHORIZATION, credentials("admin", "admin"))
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("3");
    }

    @Test
    public void should_increase_maxNumberOfUsers() {
        Response response = elevatorServerRule.target
                .path("/admin/increaseMaxNumberOfUsers").request()
                .header(AUTHORIZATION, credentials("admin", "admin"))
                .buildGet().invoke();
        assertThat(response.readEntity(String.class)).isEqualTo("4");
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

    @Test
    public void should_register_with_score() {
        String password = null;
        try {
            password = elevatorServerRule.target.path("/player/register-with-score")
                    .queryParam("email", "player@provider.com")
                    .queryParam("pseudo", "player")
                    .queryParam("serverURL", "http://localhost")
                    .queryParam("score", 493).request()
                    .header(AUTHORIZATION, credentials("admin", "admin"))
                    .buildPost(null).invoke().readEntity(String.class);

            final Response playerInfo = elevatorServerRule.target.path("/player/info")
                    .queryParam("email", "player@provider.com")
                    .request()
                    .header(AUTHORIZATION, credentials("player@provider.com", password))
                    .buildGet().invoke();

            assertThat(playerInfo.readEntity(String.class)).contains("\"score\":493");
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

    @Test
    public void should_dump_players() {
        String passwordPlayerOne = null, passwordPlayerTwo = null;
        try {
            passwordPlayerOne = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player1@provider.com")
                    .queryParam("pseudo", "player1")
                    .queryParam("serverURL", "http://localhost:8081").request()
                    .buildPost(null).invoke().readEntity(String.class);
            passwordPlayerTwo = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player2@provider.com")
                    .queryParam("pseudo", "player2")
                    .queryParam("serverURL", "http://localhost:8082").request()
                    .buildPost(null).invoke().readEntity(String.class);

            final Response playerAsCSV = elevatorServerRule.target.path("/players.csv")
                    .request()
                    .header(AUTHORIZATION, credentials("", "admin"))
                    .buildGet().invoke();

            assertThat(playerAsCSV.getHeaderString(CONTENT_TYPE)).isEqualTo("text/csv; charset=UTF-8");
            assertThat(playerAsCSV.readEntity(String.class)).isEqualTo("\"player1@provider.com\",\"player1\",\"http://localhost:8081\",0\n\"player2@provider.com\",\"player2\",\"http://localhost:8082\",0");
        } finally {
            if (passwordPlayerOne != null) {
                elevatorServerRule.target
                        .path("/player/unregister")
                        .queryParam("email", "player1@provider.com").request()
                        .header(AUTHORIZATION, credentials("player1@provider.com", passwordPlayerOne))
                        .buildPost(null).invoke();
            }
            if (passwordPlayerTwo != null) {
                elevatorServerRule.target
                        .path("/player/unregister")
                        .queryParam("email", "player2@provider.com").request()
                        .header(AUTHORIZATION, credentials("player2@provider.com", passwordPlayerTwo))
                        .buildPost(null).invoke();
            }
        }
    }

    @Test
    public void should_not_restore_existing_players() {
        String passwordPlayer = null;
        try {
            passwordPlayer = elevatorServerRule.target.path("/player/register")
                    .queryParam("email", "player@provider.com")
                    .queryParam("pseudo", "player")
                    .queryParam("serverURL", "http://localhost:8081").request()
                    .buildPost(null).invoke().readEntity(String.class);
            final FileDataBodyPart filePart = new FileDataBodyPart("players", new File("src/test/resources/players.csv"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
            final Response playerAsCSV = elevatorServerRule.target.path("/players.csv")
                    .request()
                    .header(AUTHORIZATION, credentials("", "admin"))
                    .buildPost(Entity.entity(multipart, multipart.getMediaType())).invoke();

            assertThat(playerAsCSV.getHeaderString(CONTENT_TYPE)).isEqualTo("application/json");
            String jsonPlayer = playerAsCSV.readEntity(String.class);
            assertThat(jsonPlayer).isEqualTo("{\"player@provider.com\":[\"a game with player player@provider.com has already been added\"]}");
            final Response playerInfo = elevatorServerRule.target.path("/player/info")
                    .queryParam("email", "player@provider.com")
                    .request()
                    .header(AUTHORIZATION, credentials("player@provider.com", passwordPlayer))
                    .buildGet().invoke();
            assertThat(playerInfo.readEntity(String.class)).contains("{\"pseudo\":\"player\",\"email\":\"player@provider.com\",\"score\":0");
        } finally {
            if (passwordPlayer != null) {
                elevatorServerRule.target
                        .path("/player/unregister")
                        .queryParam("email", "player@provider.com").request()
                        .header(AUTHORIZATION, credentials("player@provider.com", passwordPlayer))
                        .buildPost(null).invoke();
            }
        }
    }

    @Test
    public void should_restore_players() {
        String passwordPlayer = null;
        try {
            final FileDataBodyPart filePart = new FileDataBodyPart("players", new File("src/test/resources/players.csv"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
            final Response playerAsCSV = elevatorServerRule.target.path("/players.csv")
                    .request()
                    .header(AUTHORIZATION, credentials("", "admin"))
                    .buildPost(Entity.entity(multipart, multipart.getMediaType())).invoke();

            assertThat(playerAsCSV.getHeaderString(CONTENT_TYPE)).isEqualTo("application/json");
            String jsonPlayer = playerAsCSV.readEntity(String.class);
            assertThat(jsonPlayer).startsWith("{\"player@provider.com\":[\"").endsWith("\"]}");
            passwordPlayer = jsonPlayer.substring("{\"player@provider.com\":[\"".length(), jsonPlayer.length() - "\"]}".length());
            final Response playerInfo = elevatorServerRule.target.path("/player/info")
                    .queryParam("email", "player@provider.com")
                    .request()
                    .header(AUTHORIZATION, credentials("player@provider.com", passwordPlayer))
                    .buildGet().invoke();
            assertThat(playerInfo.readEntity(String.class)).contains("{\"pseudo\":\"plâyer\",\"email\":\"player@provider.com\",\"score\":42");
        } finally {
            if (passwordPlayer != null) {
                elevatorServerRule.target
                        .path("/player/unregister")
                        .queryParam("email", "player@provider.com").request()
                        .header(AUTHORIZATION, credentials("player@provider.com", passwordPlayer))
                        .buildPost(null).invoke();
            }
        }
    }

    private String credentials(String user, String password) {
        return "Basic " + printBase64Binary((user + ":" + password).getBytes());
    }

}
