package elevator.server;

import org.junit.Test;

import java.net.URL;

public class ElevatorGameTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_not_create_elevator_game_with_other_protocol_than_http() throws Exception {
        new ElevatorGame(null, new URL("https://127.0.0.1"));
    }

}
