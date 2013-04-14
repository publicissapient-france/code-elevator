package elevator.server;

import elevator.Command;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;

import static elevator.Command.OPEN;
import static elevator.Direction.UP;
import static java.lang.Thread.sleep;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HTTPElevatorTest {

    @Mock
    private URLConnection mock;

    @Test
    public void should_call_server_with_call() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://localhost:8080"),
                new MockURLStreamHandler("http://localhost:8080/call?atFloor=4&to=UP", mock));

        httpElevator.call(4, UP);

        sleep(30);
        verify(mock).getInputStream();
    }

    @Test
    public void should_call_server_with_go() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"),
                new MockURLStreamHandler("http://127.0.0.1/go?floorToGo=3", mock));

        httpElevator.go(3);

        sleep(30);
        verify(mock).getInputStream();
    }

    @Test
    public void should_call_server_with_reset() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://10.0.0.1/myApp/"),
                new MockURLStreamHandler("http://10.0.0.1/myApp/reset", mock));

        httpElevator.reset();

        sleep(30);
        verify(mock).getInputStream();
    }

    @Test
    public void should_call_server_with_nextCommand() throws Exception {
        when(mock.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"),
                new MockURLStreamHandler("http://127.0.0.1/nextCommand", mock));

        Command nextCommand = httpElevator.nextCommand();

        assertThat(nextCommand).isEqualTo(OPEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_server_send_illegal_command() throws Exception {
        when(mock.getInputStream()).thenReturn(new ByteArrayInputStream("_down".getBytes()));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"),
                new MockURLStreamHandler("http://127.0.0.1/nextCommand", mock));

        httpElevator.nextCommand();
    }

}
