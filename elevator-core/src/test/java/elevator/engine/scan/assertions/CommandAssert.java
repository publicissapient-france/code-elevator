package elevator.engine.scan.assertions;

import elevator.engine.scan.Command;
import org.fest.assertions.GenericAssert;

import static org.fest.assertions.Assertions.assertThat;

public class CommandAssert extends GenericAssert<CommandAssert, Command> {

    CommandAssert(Command actual) {
        super(CommandAssert.class, actual);
    }

    public CommandAssert isEqualTo(String expectedCommandAsString) {
        isNotNull();
        assertThat(actual.toString()).isEqualTo(expectedCommandAsString);
        return this;
    }

}
