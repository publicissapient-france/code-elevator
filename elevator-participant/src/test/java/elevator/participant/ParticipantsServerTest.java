package elevator.participant;

import com.sun.net.httpserver.HttpExchange;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ParticipantsServerTest {
    @After
    public void clearFakeElevatorEngineCalls() {
        FakeElevatorEngine.clearCalls();
    }

    @Test
    public void bad_request_if_no_trailing_slash() throws IOException {
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchange httpExchange = given("http://server", responseBody);
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), eq(33l));
        assertThat(responseBody.toString()).isEqualTo(" doesn't conform to \"^(.*)(/.+)$\"");
    }

    @Test
    public void bad_request_if_no_path() throws IOException {
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchange httpExchange = given("http://server/", responseBody);
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), eq(34l));
        assertThat(responseBody.toString()).isEqualTo("/ doesn't conform to \"^(.*)(/.+)$\"");
    }

    @Test
    public void bad_request_if_unknown_elevator() throws IOException {
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchange httpExchange = given("http://server/UnknownElevator/reset?cause=because", responseBody);
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(400), eq(55l));
        assertThat(responseBody.toString()).isEqualTo("Elevator implementation \"UnknownElevator\" was not found");
    }

    @Test
    public void ok_if_unknown_path() throws IOException {
        HttpExchange httpExchange = given("http://server/FakeElevatorEngine/userHasEntered");
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(200), eq(0l));
        assertThat(FakeElevatorEngine.calls()).isEmpty();
    }

    @Test
    public void should_call() throws IOException {
        HttpExchange httpExchange = given("http://server/FakeElevatorEngine/call?atFloor=0&to=UP");
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(200), eq(0l));
        assertThat(FakeElevatorEngine.calls()).isEqualTo("call(0, UP)");
    }

    @Test
    public void should_go() throws IOException {
        HttpExchange httpExchange = given("http://server/FakeElevatorEngine/go?floorToGo=3");
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(200), eq(0l));
        assertThat(FakeElevatorEngine.calls()).isEqualTo("go(3)");
    }

    @Test
    public void should_nextCommand() throws IOException {
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchange httpExchange = given("http://server/FakeElevatorEngine/nextCommand", responseBody);
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(200), eq(7l));
        assertThat(responseBody.toString()).isEqualTo("NOTHING");
        assertThat(FakeElevatorEngine.calls()).isEqualTo("nextCommand : NOTHING");
    }

    @Test
    public void should_reset() throws IOException {
        HttpExchange httpExchange = given("http://server/FakeElevatorEngine/reset?cause=because");
        ParticipantsServer participantsServer = new ParticipantsServer();

        participantsServer.handle(httpExchange);

        verify(httpExchange).sendResponseHeaders(eq(200), eq(0l));
        assertThat(FakeElevatorEngine.calls()).isEqualTo("reset(because)");
    }

    private HttpExchange given(String requestURI) {
        return given(requestURI, null);
    }

    private HttpExchange given(String requestURI, ByteArrayOutputStream responseBody) {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestURI()).thenReturn(URI.create(requestURI));
        if (responseBody != null) {
            when(httpExchange.getResponseBody()).thenReturn(responseBody);
        }
        return httpExchange;
    }
}
