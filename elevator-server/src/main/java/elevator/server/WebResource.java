package elevator.server;

import elevator.server.security.AdminAuthorization;
import elevator.server.security.UserAuthorization;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
public class WebResource {

    private final ElevatorServer server;

    public WebResource(ElevatorServer server) {
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
    @Path("/player/pause")
    @UserAuthorization
    public void pauseParticipant(@QueryParam("email") String email) {
        server.pauseElevatorGame(email);
    }

    @POST
    @Path("/player/resume")
    @UserAuthorization
    public void resumeParticipant(@QueryParam("email") String email) {
        server.resumeElevatorGame(email);
    }

    @POST
    @Path("/player/unregister")
    @UserAuthorization
    public void unregisterParticipant(@QueryParam("email") String email) {
        server.removeElevatorGame(email);
    }

    @POST
    @Path("/player/reset")
    @UserAuthorization
    public void resetPlayer(@QueryParam("email") String email) {
        server.resetPlayer(email);
    }

    @GET
    @Path("/player/info")
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public PlayerInfo playerInfo(@QueryParam("email") String email) {
        return server.getPlayerInfo(email);
    }

    @GET
    @Path("/leaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<PlayerInfo> leaderboard() {
        Collection<PlayerInfo> players = new ArrayList<>();
        for (ElevatorGame game : server.getUnmodifiableElevatorGames()) {
            players.add(game.getPlayerInfo());
        }
        return players;
    }

    @GET
    @Path("/admin/maxNumberOfUsers")
    @AdminAuthorization
    public String getMaxNumberOfUsers() {
        return String.valueOf(server.getMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/increaseMaxNumberOfUsers")
    @AdminAuthorization
    public String increaseMaxNumberOfUsers() {
        return String.valueOf(server.increaseMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/decreaseMaxNumberOfUsers")
    @AdminAuthorization
    public String decreaseMaxNumberOfUsers() {
        return String.valueOf(server.decreaseMaxNumberOfUsers());
    }

    @POST
    @Path("/admin/removeElevatorGame")
    @AdminAuthorization
    public void removeElevatorGame(@QueryParam("email") String email) {
        server.removeElevatorGame(email);
    }

}
