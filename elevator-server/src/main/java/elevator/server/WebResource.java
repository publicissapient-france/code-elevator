package elevator.server;

import elevator.server.security.AdminAuthentication;
import elevator.server.security.UserAuthentication;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Path("/")
public class WebResource {
    private final ElevatorServer server;

    WebResource(ElevatorServer server) {
        this.server = server;
    }

    @POST
    @Path("/player/register")
    public String newParticipant(@QueryParam("email") String email, @QueryParam("pseudo") String pseudo,
                                 @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        try {
            Player player = new Player(email, pseudo);
            server.addElevatorGame(player, new URL(serverURL));
            return player.password.value();
        } catch (IllegalStateException | MalformedURLException e) {
            throw new WebApplicationException(e, Response.status(FORBIDDEN).entity(e.getMessage()).build());
        }
    }

    @POST
    @Path("/player/register-with-score")
    @AdminAuthentication
    public String newParticipantWithScore(@QueryParam("email") String email,
                                          @QueryParam("pseudo") String pseudo,
                                          @QueryParam("serverURL") String serverURL,
                                          @QueryParam("score") Integer score) throws WebApplicationException {
        try {
            Player player = new Player(email, pseudo);
            server.addElevatorGame(player, new URL(serverURL), new Score(score));
            return player.password.value();
        } catch (IllegalStateException | MalformedURLException e) {
            throw new WebApplicationException(e, Response.status(FORBIDDEN).entity(e.getMessage()).build());
        }
    }

    @GET
    @Path("/players.csv")
    @Produces("text/csv;charset=UTF-8")
    @AdminAuthentication
    public String players() {
        return server.getUnmodifiableElevatorGames().stream().map(input -> "" +
                        "\"" + input.getPlayerInfo().email + "\"," +
                        "\"" + input.getPlayerInfo().pseudo + "\"," +
                        "\"" + input.url + "\"," +
                        input.score().toString())
                .collect(joining("\n"));
    }

    @POST
    @Path("/players.csv")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @AdminAuthentication
    public Map<String, Collection<String>> importPlayers(
            @FormDataParam("players") InputStream uploadedInputStream) throws IOException {
        final Map<String, Collection<String>> passwordsByEmail = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(uploadedInputStream, Charset.forName("UTF-8")))) {
            String s;
            while ((s = in.readLine()) != null) {
                List<String> columns = stream(s.split(",")).map(column -> column.replaceAll("\"(.*)\"", "$1")).collect(toList());
                String email = columns.get(0);
                String pseudo = columns.get(1);
                String serverURL = columns.get(2);
                Integer score = Integer.parseInt(columns.get(3));
                try {
                    add(passwordsByEmail, email, newParticipantWithScore(email, pseudo, serverURL, score));
                } catch (WebApplicationException e) {
                    add(passwordsByEmail, email, e.getResponse().getEntity().toString());
                }
            }
        }
        return passwordsByEmail;
    }

    private void add(Map<String, Collection<String>> passwordsByEmail, String email, String newParticipantWithScore) {
        if (!passwordsByEmail.containsKey(email)) {
            passwordsByEmail.put(email, new ArrayList<>());
        }
        passwordsByEmail.get(email).add(newParticipantWithScore);
    }

    @POST
    @Path("/player/pause")
    @UserAuthentication
    public void pauseParticipant(@Context SecurityContext securityContext) {
        server.pauseElevatorGame(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Path("/player/resume")
    @UserAuthentication
    public void resumeParticipant(@Context SecurityContext securityContext) {
        server.resumeElevatorGame(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Path("/player/unregister")
    @UserAuthentication
    public void unregisterParticipant(@Context SecurityContext securityContext) {
        server.removeElevatorGame(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Path("/player/reset")
    @UserAuthentication
    public void resetPlayer(@Context SecurityContext securityContext) {
        server.resetPlayer(securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("/player/info")
    @Produces(APPLICATION_JSON)
    @UserAuthentication
    public PlayerInfo playerInfo(@Context SecurityContext securityContext) {
        return server.getPlayerInfo(securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("/leaderboard")
    @Produces(APPLICATION_JSON)
    public List<PlayerInfo> leaderboard() {
        return server.getUnmodifiableElevatorGames()
                .stream()
                .map(ElevatorGame::getPlayerInfo)
                .collect(toList());
    }

    @GET
    @Path("/admin/maxNumberOfUsers")
    @AdminAuthentication
    public String getMaxNumberOfUsers() {
        return String.valueOf(server.getMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/increaseMaxNumberOfUsers")
    @AdminAuthentication
    public String increaseMaxNumberOfUsers() {
        return String.valueOf(server.increaseMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/decreaseMaxNumberOfUsers")
    @AdminAuthentication
    public String decreaseMaxNumberOfUsers() {
        return String.valueOf(server.decreaseMaxNumberOfUsers());
    }

    @POST
    @Path("/admin/removeElevatorGame")
    @AdminAuthentication
    public void removeElevatorGame(@QueryParam("email") String email) {
        server.removeElevatorGame(email);
    }
}
