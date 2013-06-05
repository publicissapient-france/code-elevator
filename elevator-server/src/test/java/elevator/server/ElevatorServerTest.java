package elevator.server;

import elevator.server.port.Port;
import elevator.server.port.SystemPropertyRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static elevator.server.port.PortFactory.ELEVATOR_SERVER_PORT_PROPERTY;
import static org.fest.assertions.Assertions.assertThat;

public class ElevatorServerTest {

    @Rule
    public SystemPropertyRule systemPropertyRule = new SystemPropertyRule(ELEVATOR_SERVER_PORT_PROPERTY);

    @Test
    public void should_start_with_random_port() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();

        Port port = elevatorServer.start();

        try (InputStream in = new URL("http", "localhost", port.port(), "/resources/players").openConnection().getInputStream()) {
            BufferedReader content = new BufferedReader(new InputStreamReader(in));
            assertThat(content.readLine()).isEqualTo("[]");
        }
    }

    @Test
    @Ignore
    public void should_start_with_given_port() throws Exception {
        System.setProperty(ELEVATOR_SERVER_PORT_PROPERTY, "8080");
        ElevatorServer elevatorServer = new ElevatorServer();

        Port port = elevatorServer.start();

        assertThat(port.port()).isEqualTo(8080);
    }

}
