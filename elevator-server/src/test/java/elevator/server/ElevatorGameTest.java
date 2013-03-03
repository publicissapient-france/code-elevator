package elevator.server;

import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class ElevatorGameTest {

    @Test
    public void should_add_user() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8080")));

        elevatorGame.addUser();

        assertThat(elevatorGame.users()).hasSize(1);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_more_than_ten_users() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Elevator(new Email("email@provider.com"), new URL("http://127.0.0.1:8080")));
        for (Integer i = 1; i <= 10; i++) {
            elevatorGame.addUser();
        }

        assertThat(elevatorGame.users()).hasSize(10);
        elevatorGame.addUser();
        fail();
    }

}
