package elevator.engine.scan;

import java.util.Set;

public class CommandsAccessor {

    private final Commands commands;

    public CommandsAccessor(Commands commands) {
        this.commands = commands;
    }

    public Set<Command> commands() {
        return commands.commands();
    }

}
