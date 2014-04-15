package elevator.server;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import elevator.server.security.AdminAuthentication;
import elevator.server.security.UserAuthentication;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.CharMatcher.is;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Joiner.on;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.CharStreams.readLines;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
public class WebResource {
	public static final String ZERO = "0";
	public static final String INT_MAX_VALUE = "2147483647";
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
    @Produces("text/csv; charset=UTF-8")
    @AdminAuthentication
    public String players() {
        return on('\n').join(from(server.getUnmodifiableElevatorGames()).transform(new Function<ElevatorGame, String>() {
            @Override
            public String apply(ElevatorGame input) {
                return on(',').join(newArrayList(
                        "\"" + input.getPlayerInfo().email + "\"",
                        "\"" + input.getPlayerInfo().pseudo + "\"",
                        "\"" + input.url + "\"",
                        input.score().toString()));
            }
        }));
    }

    @POST
    @Path("/players.csv")
    @Consumes(MULTIPART_FORM_DATA)
    @AdminAuthentication
    public Map<String, Collection<String>> importPlayers(
            @FormDataParam("players") InputStream uploadedInputStream) throws IOException {
        List<String> fileContent;
        try (InputStreamReader inputStreamReader = new InputStreamReader(uploadedInputStream, UTF_8)) {
            fileContent = readLines(inputStreamReader);
        }
        final ListMultimap<String, String> passwordsByEmail = ArrayListMultimap.create();
        for (String s : fileContent) {
            List<String> columns = Splitter.on(",").trimResults(is('"')).splitToList(s);
            String email = columns.get(0);
            String pseudo = columns.get(1);
            String serverURL = columns.get(2);
            Integer score = Integer.parseInt(columns.get(3));
            try {
                passwordsByEmail.put(email, newParticipantWithScore(email, pseudo, serverURL, score));
            } catch (WebApplicationException e) {
                passwordsByEmail.put(email, e.getResponse().getEntity().toString());
            }
        }
        return passwordsByEmail.asMap();
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
    public Collection<PlayerInfo> leaderboard() {
        Collection<PlayerInfo> players = new ArrayList<>();
        for (ElevatorGame game : server.getUnmodifiableElevatorGames()) {
            players.add(game.getPlayerInfo());
        }
        return players;
    }

	@GET
	@Path("/leaderboard/hallOfFame")
	@Produces(APPLICATION_JSON)
	public List<ScoreInfo> hallOfFame() {
		return server.getMaxScores();
	}

	@GET
	@Path("/leaderboard/hallOfFame/{startOfRange}:{endOfRange}")
	@Produces(APPLICATION_JSON)
	public List<ScoreInfo> hallOfFame(
			@PathParam("startOfRange") @DefaultValue("0") int startOfRange,
			@PathParam("endOfRange") @DefaultValue(INT_MAX_VALUE) int endOfRange) {

		return server.getMaxScores(startOfRange, endOfRange);
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
