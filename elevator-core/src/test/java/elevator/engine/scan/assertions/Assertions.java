package elevator.engine.scan.assertions;

import elevator.engine.scan.Command;
import elevator.engine.scan.Commands;

public class Assertions {

    public static CommandAssert assertThat(Command actual) {
        return new CommandAssert(actual);
    }

    public static CommandsAssert assertThat(Commands actual) {
        return new CommandsAssert(actual);
    }

}
