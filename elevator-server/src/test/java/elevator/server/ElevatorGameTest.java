package elevator.server;

import elevator.Clock;
import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class ElevatorGameTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_not_create_elevator_game_with_other_protocol_than_http() throws Exception {
        new ElevatorGame(null, new URL("https://127.0.0.1"), null, new Clock());
    }

    @Test
    public void should_loose_and_update_message_when_reset() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, new Clock());

        elevatorGame.reset("error message");

        assertThat(elevatorGame.score.score).isEqualTo(-10);
        assertThat(elevatorGame.lastErrorMessage).isEqualTo("error message");
    }

}
