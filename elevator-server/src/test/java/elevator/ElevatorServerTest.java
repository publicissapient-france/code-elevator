package elevator;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ElevatorServerTest {

    @Test
    public void should_start() {
        ElevatorServer elevatorServer = new ElevatorServer();

        StartedElevatorServer startedElevatorServer = elevatorServer.start();

        assertThat(startedElevatorServer).isNotNull();
    }

}
