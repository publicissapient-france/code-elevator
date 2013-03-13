package elevator.client.assertions;

import elevator.client.Command;
import elevator.client.Commands;

public class Assertions {

    public static CommandAssert assertThat(Command actual) {
        return new CommandAssert(actual);
    }

    public static CommandsAssert assertThat(Commands actual) {
        return new CommandsAssert(actual);
    }

}
