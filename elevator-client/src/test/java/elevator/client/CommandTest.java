package elevator.client;

import elevator.client.Command;
import org.junit.Test;

import static elevator.client.Direction.UP;

public class CommandTest {

    @Test(expected = NullPointerException.class)
    public void should_not_create_command_without_stage() throws Exception {
        new Command(null, UP);
    }

    @Test(expected = NullPointerException.class)
    public void should_not_create_command_without_direction() throws Exception {
        new Command(3, null);
    }

}
