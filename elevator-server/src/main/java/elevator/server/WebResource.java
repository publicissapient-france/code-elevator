package elevator.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Path("/")
public class WebResource {

    private final StartedElevatorServer server;

    public WebResource(StartedElevatorServer server) {
        this.server = server;
    }

    @POST
    @Path("/new-participant")
    public void newParticipant(@QueryParam("player") String email, @QueryParam("pseudo") String pseudo, @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        server.addElevatorGame(new Player(email,pseudo), new URL(serverURL));
    }

    @POST
    @Path("/unregister-participant")
    public void unregisterNewParticipant(@QueryParam("serverURL") String email) throws MalformedURLException {
        server.removeElevatorGame(new Player(email));
    }

    @GET
    @Path("/emails")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Player> emails() {
        return server.emails();
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
