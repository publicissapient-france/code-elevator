package elevator.server;

import elevator.user.ConstantMaxNumberOfUsers;
import elevator.user.FloorsAndDirection;
import elevator.user.InitializationStrategy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static elevator.server.ElevatorGame.State.PAUSE;
import static elevator.server.ElevatorGame.State.RESUME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorGameTest {
    @Mock
    private URLConnection urlConnection;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private URL url;

    @Before
    public void mockURLConnection() {
        try {
            when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Before
    public void createURL() {
        try {
            url = new URL("http://127.0.0.1");
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    public void should_not_create_elevator_game_with_other_protocol_than_http() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("http is the only supported protocol");

        new ElevatorGame(null, new URL("https://127.0.0.1"), null, null);
    }

    @Test
    public void should_get_player_info() throws Exception {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), url, null, new Score(45), () -> new FloorsAndDirection(0, 1), new DontConnectURLStreamHandler(urlConnection));

        PlayerInfo playerInfo = elevatorGame.getPlayerInfo();

        assertThat(playerInfo.doorIsOpen).isFalse();
        assertThat(playerInfo.elevatorAtFloor).isZero();
        assertThat(playerInfo.email).isEqualTo("player@provider.com");
        assertThat(playerInfo.lastErrorMessage).isNull();
        assertThat(playerInfo.peopleInTheElevator).isZero();
        assertThat(playerInfo.pseudo).isEqualTo("player");
        assertThat(playerInfo.score).isEqualTo(45);
        assertThat(playerInfo.state).isEqualTo("INIT");
    }

    @Test
    public void should_loose_and_update_message_when_reset() throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), url, null, new Score(), () -> new FloorsAndDirection(0, 1), new DontConnectURLStreamHandler(urlConnection));
        elevatorGame.resume();

        elevatorGame.reset("error message");

        assertThat(elevatorGame.score()).isEqualTo(-10);
        assertThat(elevatorGame.lastErrorMessage).isEqualTo("error message");
    }

    @Test
    public void should_stop() throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), url, null, new Score(), () -> new FloorsAndDirection(0, 1), new DontConnectURLStreamHandler(urlConnection));

        elevatorGame.stop();

        assertThat(elevatorGame.state).isEqualTo(PAUSE);
    }

    @Test
    public void should_resume() throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), url, null, new Score(), () -> new FloorsAndDirection(0, 1), new DontConnectURLStreamHandler(urlConnection)).stop();

        elevatorGame.resume();

        assertThat(elevatorGame.state).isEqualTo(RESUME);
    }

    @Test
    public void should_compute_score_even_if_user_have_not_wait_at_all() throws IOException {
        ElevatorGame elevatorGame = new ElevatorGame(new Player("player@provider.com", "player"), url, new ConstantMaxNumberOfUsers(2), new Score(), new InitializationStrategy() {

            private Queue<Integer> initialFloor = new ArrayDeque<>(Arrays.asList(4, 0, 4));

            @Override
            public FloorsAndDirection create() {
                return new FloorsAndDirection(initialFloor.poll(), 1);
            }

        }, new DontConnectURLStreamHandler(urlConnection));
        elevatorGame.updateState();
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        elevatorGame.updateState();
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("CLOSE".getBytes()));
        elevatorGame.updateState();
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("UP".getBytes()));
        elevatorGame.updateState();
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        elevatorGame.updateState();

        assertThat(elevatorGame.score()).isEqualTo(20);
    }
}
