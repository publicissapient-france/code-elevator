package elevator.server;

import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class StartedElevatorServerTest {

    @Test
    public void should_add_elevator_game() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.addElevatorGame(new Email("email@provider.com"), new URL("http://127.0.0.1"));

        assertThat(startedElevatorServer.elevatorGames()).hasSize(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_not_add_elevator_game_if_protocol_is_not_http() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.addElevatorGame(new Email("email@provider.com"), new URL("https://127.0.0.1"));

        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_elevator_game_with_same_email_twice() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.
                addElevatorGame(new Email("email@provider.com"), new URL("http://127.0.0.1")).
                addElevatorGame(new Email("email@provider.com"), new URL("http://127.0.0.1:8080/myApp"));

        fail();
    }

}
