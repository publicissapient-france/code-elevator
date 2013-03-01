package elevator.server;

import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class StartedElevatorServerTest {

    @Test
    public void should_add_elevator_game() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.
                addElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8080")));

        assertThat(startedElevatorServer.elevatorGames()).hasSize(1);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_elevator_game_whith_same_email_twice() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.
                addElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8080"))).
                addElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8081")));

        fail();
    }

}
