package elevator.engine.scan;

import org.junit.Before;
import org.junit.Test;

import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static elevator.engine.scan.ElevatorDirection.DOWN;
import static elevator.engine.scan.ElevatorDirection.UP;
import static elevator.engine.scan.assertions.Assertions.assertThat;

public class CommandsTest {

    private Commands commands;

    @Before
    public void initCommands() throws Exception {
        commands = new Commands(LOWER_FLOOR, HIGHER_FLOOR);
    }

    @Test
    public void should_tell_if_there_is_no_command() throws Exception {
        assertThat(commands).isEmpty();
    }

    @Test
    public void should_never_return_command_when_there_is_no_commands() throws Exception {
        assertThat(commands.get(1, UP)).isNull();
    }

    @Test
    public void should_get_the_only_one_command() throws Exception {
        commands.add(new Command(3, UP));

        assertThat(commands.get(0, UP)).isEqualTo("3 UP");
    }

    @Test
    public void should_get_closest_command_in_direction_to_first_command() throws Exception {
        commands.add(new Command(4, DOWN))
                .add(new Command(3, UP))
                .add(new Command(5, DOWN));

        assertThat(commands.get(1, UP)).isEqualTo("3 UP");
    }

}
