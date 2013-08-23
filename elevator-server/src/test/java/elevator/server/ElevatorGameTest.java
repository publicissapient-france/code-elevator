package elevator.server;

import elevator.Clock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorGameTest {

    @Spy
    private Clock clock;

    @Test(expected = IllegalArgumentException.class)
    public void should_not_create_elevator_game_with_other_protocol_than_http() throws Exception {
        new ElevatorGame(null, new URL("https://127.0.0.1"), null, clock);
    }

    @Test
    public void should_get_player_info() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock);

        PlayerInfo playerInfo = elevatorGame.getPlayerInfo();

        assertThat(playerInfo.doorIsOpen).isFalse();
        assertThat(playerInfo.elevatorAtFloor).isZero();
        assertThat(playerInfo.email).isEqualTo("player@provider.com");
        assertThat(playerInfo.lastErrorMessage).isNull();
        assertThat(playerInfo.peopleInTheElevator).isZero();
        assertThat(playerInfo.peopleWaitingTheElevator).isEqualTo(new int[]{0, 0, 0, 0, 0, 0});
        assertThat(playerInfo.pseudo).isEqualTo("player");
        assertThat(playerInfo.score).isZero();
        assertThat(playerInfo.state).isEqualTo("RESUME");
    }

    @Test
    public void should_loose_and_update_message_when_reset() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock);

        elevatorGame.reset("error message");

        assertThat(elevatorGame.score()).isEqualTo(-10);
        assertThat(elevatorGame.lastErrorMessage).isEqualTo("error message");
    }

    @Test
    public void should_stop() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock);

        elevatorGame.stop();

        verify(clock, times(1)).removeClockListener(elevatorGame);
        assertThat(elevatorGame.getPlayerInfo().state).isEqualTo("PAUSE");
    }

    @Test
    public void should_resume() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock).stop();

        elevatorGame.resume();

        verify(clock, times(2)).addClockListener(elevatorGame);
    }

}
