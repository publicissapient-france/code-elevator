package elevator.server;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class StartedElevatorServerTest {

    @Test
    public void should_add_elevator_game() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.
                addElevatorGame(new Email("email@provider.com"));

        assertThat(startedElevatorServer.elevatorGames()).hasSize(1);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_elevator_game_whith_same_email_twice() throws Exception {
        StartedElevatorServer startedElevatorServer = new StartedElevatorServer();

        startedElevatorServer.
                addElevatorGame(new Email("email@provider.com")).
                addElevatorGame(new Email("email@provider.com"));

        fail();
    }

}
