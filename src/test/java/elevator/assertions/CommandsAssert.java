package elevator.assertions;

import elevator.Command;
import elevator.Commands;
import elevator.CommandsAccessor;
import org.fest.assertions.GenericAssert;

import static org.fest.assertions.Assertions.assertThat;

public class CommandsAssert extends GenericAssert<CommandsAssert, Commands> {

    private final CommandsAccessor commandsAccessor;

    CommandsAssert(Commands actual) {
        super(CommandsAssert.class, actual);
        commandsAccessor = new CommandsAccessor(actual);
    }

    public CommandsAssert isEmpty() {
        assertThat(commandsAccessor.commands()).isEmpty();

        return this;
    }

    public CommandsAssert contains(Command expectedCommandPresent) {
        assertThat(commandsAccessor.commands()).contains(expectedCommandPresent);
        return this;
    }

    public CommandsAssert excludes(Command expectedCommandNotPresent) {
        assertThat(commandsAccessor.commands()).excludes(expectedCommandNotPresent);
        return this;
    }
}
