package elevator.server;

import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class ElevatorGameTest {

    @Test
    public void should_add_user() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8080")));

        elevatorGame.addUser();

        assertThat(elevatorGame.users()).hasSize(1);
    }

}
