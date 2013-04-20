package elevator.server;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static org.fest.assertions.Assertions.assertThat;

public class ElevatorServerTest {

    @Test
    public void should_start() throws Exception {
        ElevatorServer elevatorServer = new ElevatorServer();

        RandomPort port = elevatorServer.start();

        try (InputStream in = new URL("http", "localhost", port.port, "/emails").openConnection().getInputStream()) {
            BufferedReader content = new BufferedReader(new InputStreamReader(in));
            assertThat(content.readLine()).isEqualTo("[]");
        }

    }

}
