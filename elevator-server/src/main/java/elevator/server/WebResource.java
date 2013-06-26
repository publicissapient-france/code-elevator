package elevator.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
public class WebResource {

    private final StartedElevatorServer server;

    public WebResource(StartedElevatorServer server) {
        this.server = server;
    }

    @POST
    @Path("/player/register")
    public void newParticipant(@QueryParam("email") String email, @QueryParam("pseudo") String pseudo,
                               @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        try {
            server.addElevatorGame(new Player(email, pseudo), new URL(serverURL));
        } catch (IllegalStateException e) {
            throw new WebApplicationException(e, Response.status(FORBIDDEN).entity(e.getMessage()).build());
        }
    }

    @POST
    @Path("/player/unregister")
    public void unregisterNewParticipant(@QueryParam("email") String email) {
        server.removeElevatorGame(email);
    }

    @GET
    @Path("/players")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Player> players() {
        return server.players();
    }

    @GET
    @Path("/player/info/")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerInfo infoForPlayer(@QueryParam("email") String email) {
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

}
