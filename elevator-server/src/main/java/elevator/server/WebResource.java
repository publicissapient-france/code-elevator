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

    @GET
    @Path("/new-participant")
    public void newParticipant(@QueryParam("email") String email, @QueryParam("pseudo") String pseudo, @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        server.addElevatorGame(new Player(email,pseudo), new URL(serverURL));
    }

    @GET
    @Path("/unregister-participant")
    public void unregisterNewParticipant(@QueryParam("email") String email) throws MalformedURLException {
        server.removeElevatorGame(email);
    }

    @GET
    @Path("/players")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Player> players() {
        return server.players();
    }

    @GET
    @Path("/playerinfo/")
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
