package elevator.engine.scan.assertions;

import elevator.engine.scan.Commands;
import elevator.engine.scan.CommandsAccessor;
import org.assertj.core.api.AbstractObjectAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandsAssert extends AbstractObjectAssert<CommandsAssert, Commands> {
    private final CommandsAccessor commandsAccessor;

    CommandsAssert(Commands actual) {
        super(actual, CommandsAssert.class);
        commandsAccessor = new CommandsAccessor(actual);
    }

    public CommandsAssert isEmpty() {
        assertThat(commandsAccessor.commands()).isEmpty();

        return this;
    }
}
