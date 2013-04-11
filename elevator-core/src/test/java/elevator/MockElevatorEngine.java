package elevator;

import elevator.engine.ElevatorEngine;

import java.util.ArrayDeque;
import java.util.Queue;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

class MockElevatorEngine implements ElevatorEngine {

    private final Queue<Command> commands;
    private Boolean resetCalled = FALSE;

    MockElevatorEngine(Command... commands) {
        this.commands = new ArrayDeque<>(asList(commands));
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        return this;
    }

    @Override
    public Command nextCommand() {
        return commands.poll();
    }

    @Override
    public ElevatorEngine reset() {
        resetCalled = TRUE;
        return this;
    }

    Boolean resetCalled() {
        return resetCalled;
    }

}
