package elevator.server;

import elevator.Command;
import elevator.User;
import elevator.exception.ElevatorIsBrokenException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

import static elevator.Command.OPEN;
import static elevator.Direction.UP;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HTTPElevatorTest {

    @Mock
    private URLConnection urlConnection;

    @Mock
    private ExecutorService executorService;

    @Rule
    public ExpectedException expectedException = none();

    @Before
    public void initExecutorServiceToRunInCurrentThread() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Runnable) invocationOnMock.getArguments()[0]).run();
                return null;
            }
        }).when(executorService).execute(any(Runnable.class));
    }

    @Test
    public void should_call_server_with_call() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://localhost:8080"), executorService,
                new DontConnectURLStreamHandler("http://localhost:8080/call?atFloor=4&to=UP", urlConnection));

        httpElevator.call(4, UP);

        verify(urlConnection).getInputStream();
    }

    @Test
    public void should_call_server_with_go() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/go?floorToGo=3", urlConnection));

        httpElevator.go(3);

        verify(urlConnection).getInputStream();
    }

    @Test
    public void should_call_server_with_reset() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://10.0.0.1/myApp/"), executorService,
                new DontConnectURLStreamHandler("http://10.0.0.1/myApp/reset?cause=reason", urlConnection));

        httpElevator.reset("reason");

        verify(urlConnection).getInputStream();
    }

    @Test
    public void should_call_server_with_userHasEntered() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://10.0.0.1/myApp/"), executorService,
                new DontConnectURLStreamHandler("http://10.0.0.1/myApp/userHasEntered", urlConnection));

        httpElevator.userHasEntered(any(User.class));

        verify(urlConnection).getInputStream();
    }

    @Test
    public void should_call_server_with_userHasExited() throws Exception {
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://10.0.0.1/myApp/"), executorService,
                new DontConnectURLStreamHandler("http://10.0.0.1/myApp/userHasExited", urlConnection));

        User user = mock(User.class);
        doReturn(0).when(user).getInitialFloor();
        doReturn(1).when(user).getFloorToGo();
        httpElevator.userHasExited(user);

        verify(urlConnection).getInputStream();
    }

    @Test
    public void should_call_server_with_nextCommand() throws Exception {
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("OPEN".getBytes()));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/nextCommand", urlConnection));

        Command nextCommand = httpElevator.nextCommand();

        assertThat(nextCommand).isEqualTo(OPEN);
    }

    @Test
    public void should_throws_exception_when_server_send_illegal_command() throws Exception {
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("_down".getBytes()));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/nextCommand", urlConnection));

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("Command \"_down\" is not a valid command; valid commands are [UP|DOWN|OPEN|CLOSE|NOTHING] with case sensitive");
        httpElevator.nextCommand();
    }

    @Test
    public void should_throws_exception_when_server_send_no_command() throws Exception {
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/nextCommand", urlConnection));

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("No command was provided; valid commands are [UP|DOWN|OPEN|CLOSE|NOTHING] with case sensitive");
        httpElevator.nextCommand();
    }

    @Test
    public void should_handle_transport_error() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new IOException("connection failed"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/nextCommand", urlConnection));

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("connection failed");
        httpElevator.nextCommand();
    }

    @Test
    public void should_handle_UnknownHostException() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new UnknownHostException("fakehost"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://fakehost"), executorService,
                new DontConnectURLStreamHandler("http://fakehost/nextCommand", urlConnection));

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("IP address of \"fakehost\" could not be determined");
        httpElevator.nextCommand();
    }

    @Test
    public void should_tell_that_a_transport_error_has_occured_at_second_call_when_first_call_is_non_blocking() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new IOException("connection failed"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/call?atFloor=4&to=UP", urlConnection));
        httpElevator.call(4, UP);

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("connection failed");
        httpElevator.call(4, UP);
    }

    @Test
    public void should_handle_404_error() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new FileNotFoundException("http://localhost:8080/context/call?atFloor=4&to=UP"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://localhost:8080/context/"), executorService,
                new DontConnectURLStreamHandler("http://localhost:8080/context/call?atFloor=4&to=UP", urlConnection));
        httpElevator.call(4, UP);

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("Resource \"http://localhost:8080/context/call\" is not found");
        httpElevator.call(4, UP);
    }

    @Test
    public void should_return_url_without_query_when_server_respond_with_HTTP_status_code_error() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new IOException("Server returned HTTP response code: 500 for URL: http://localhost:8080/context/call?atFloor=4&to=UP"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://localhost:8080/context/"), executorService,
                new DontConnectURLStreamHandler("http://localhost:8080/context/call?atFloor=4&to=UP", urlConnection));
        httpElevator.call(4, UP);

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("Server returned HTTP response code: 500 for URL: http://localhost:8080/context/call");
        httpElevator.call(4, UP);
    }

    @Test
    public void should_tell_that_a_transport_error_has_occured_when_call_is_blocking() throws Exception {
        when(urlConnection.getInputStream()).thenThrow(new IOException("connection failed"));
        HTTPElevator httpElevator = new HTTPElevator(new URL("http://127.0.0.1"), executorService,
                new DontConnectURLStreamHandler("http://127.0.0.1/nextCommand", urlConnection));

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("connection failed");
        httpElevator.nextCommand();
    }

}
