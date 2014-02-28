package elevator.server;

import elevator.Clock;
import elevator.user.ConstantMaxNumberOfUsers;
import elevator.user.FloorsAndDirection;
import elevator.user.InitializationStrategy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static java.lang.Boolean.FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorGameTest {
    @Spy
    private Clock clock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_not_create_elevator_game_with_other_protocol_than_http() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("http is the only supported protocol");

        new ElevatorGame(null, new URL("https://127.0.0.1"), null, clock, null);
    }

    @Test
    public void should_get_player_info() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock, new Score(45));

        PlayerInfo playerInfo = elevatorGame.getPlayerInfo();

        assertThat(playerInfo.doorIsOpen).isFalse();
        assertThat(playerInfo.elevatorAtFloor).isZero();
        assertThat(playerInfo.email).isEqualTo("player@provider.com");
        assertThat(playerInfo.lastErrorMessage).isNull();
        assertThat(playerInfo.peopleInTheElevator).isZero();
        assertThat(playerInfo.pseudo).isEqualTo("player");
        assertThat(playerInfo.score).isEqualTo(45);
        assertThat(playerInfo.state).isEqualTo("RESUME");
    }

    @Test
    public void should_loose_and_update_message_when_reset() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock, new Score());

        elevatorGame.reset("error message");

        assertThat(elevatorGame.score()).isEqualTo(-10);
        assertThat(elevatorGame.lastErrorMessage).isEqualTo("error message");
    }

    @Test
    public void should_stop() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock, new Score());

        elevatorGame.stop(FALSE);

        verify(clock, times(1)).removeClockListener(elevatorGame);
        assertThat(elevatorGame.getPlayerInfo().state).isEqualTo("PAUSE");
    }

    @Test
    public void should_resume() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), null, clock, new Score()).stop(FALSE);

        elevatorGame.resume();

        verify(clock, times(2)).addClockListener(elevatorGame);
    }

    @Test
    public void should_compute_score_even_if_user_have_not_wait_at_all() throws IOException, InterruptedException {
        URLConnection urlConnection = mock(URLConnection.class);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), new URL("http://localhost"), new ConstantMaxNumberOfUsers(2), clock, new Score(), new InitializationStrategy() {

            private Queue<Integer> initialFloor = new ArrayDeque<>(Arrays.asList(4, 0, 4));

            @Override
            public FloorsAndDirection create() {
                return new FloorsAndDirection(initialFloor.poll(), 1);
            }

        }, new DontConnectURLStreamHandler(urlConnection));
        Thread.sleep(100);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        clock.tick();
        Thread.sleep(100);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("CLOSE".getBytes()));
        clock.tick();
        Thread.sleep(100);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("UP".getBytes()));
        clock.tick();
        Thread.sleep(100);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        clock.tick();
        Thread.sleep(100);

        assertThat(elevatorGame.score()).isEqualTo(20);
    }
}
