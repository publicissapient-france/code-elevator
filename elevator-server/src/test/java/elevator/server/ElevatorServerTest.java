package elevator.server;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class ElevatorServerTest {

    @ClassRule
    public static PlayerServerRule playerServerRule = new PlayerServerRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_add_elevator_game() throws Exception {
        Player player = new Player("player@provider.com", "pseudo");
        ElevatorServer elevatorServer = new ElevatorServer();

        elevatorServer.addElevatorGame(player, new URL("http://127.0.0.1:8080"));

        Collection<ElevatorGame> elevatorGames = elevatorServer.getUnmodifiableElevatorGames();
        assertThat(elevatorGames).hasSize(1);
        assertThat(elevatorGames.iterator().next().player).isEqualTo(player);
    }

    @Test
    public void should_not_add_elevator_game_with_same_email_twice() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("a game with player player@provider.com has already been added");

        elevatorServer.
                addElevatorGame(new Player("player@provider.com", "pseudo1"), new URL("http://127.0.0.1:8080")).
                addElevatorGame(new Player("player@provider.com", "pseudo2"), new URL("http://127.0.0.1:8080/myApp"));
    }

    @Test
    public void should_loose_and_give_message_when_user_wants_to_reset() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();
        elevatorServer.addElevatorGame(new Player("player@provider.com", "pseudo"), new URL("http://127.0.0.1:8080"));

        elevatorServer.resetPlayer("player@provider.com");

        PlayerInfo playerInfo = elevatorServer.getPlayerInfo("player@provider.com");
        assertThat(playerInfo.lastErrorMessage).isEqualTo("player has requested a reset");
        assertThat(playerInfo.score).isEqualTo(-10);
    }

    @Test
    public void should_remove_elevator_game() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();
        elevatorServer.addElevatorGame(new Player("player@provider.com", "pseudo"), new URL("http://127.0.0.1:8080"));

        elevatorServer.removeElevatorGame("player@provider.com");

        assertThat(elevatorServer.getUnmodifiableElevatorGames()).isEmpty();
    }

    @Test
    public void should_resume_elevator_game() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();
        elevatorServer.addElevatorGame(new Player("player@provider.com", "pseudo"), new URL("http://127.0.0.1:8080")).pauseElevatorGame("player@provider.com");

        elevatorServer.resumeElevatorGame("player@provider.com");

        assertThat(elevatorServer.getUnmodifiableElevatorGames()).hasSize(1);
    }

}
