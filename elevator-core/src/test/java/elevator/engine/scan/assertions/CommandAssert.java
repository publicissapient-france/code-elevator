package elevator.engine.scan.assertions;

import elevator.engine.scan.Command;
import org.assertj.core.api.AbstractObjectAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandAssert extends AbstractObjectAssert<CommandAssert, Command> {
    CommandAssert(Command actual) {
        super(actual, CommandAssert.class);
    }

    public CommandAssert isEqualTo(String expectedCommandAsString) {
        isNotNull();
        assertThat(actual.toString()).isEqualTo(expectedCommandAsString);
        return this;
    }
}
