package elevator.participant;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.User;

import static elevator.Command.NOTHING;
import static java.lang.String.format;

public class FakeElevatorEngine implements ElevatorEngine {
    private static final StringBuilder calls = new StringBuilder();

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException {
        calls.append(format("call(%d, %s)", atFloor, to));
        return null;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException {
        calls.append(format("go(%d)", floorToGo));
        return null;
    }

    @Override
    public Command nextCommand() throws ElevatorIsBrokenException {
        Command nextCommand = NOTHING;
        calls.append(format("nextCommand : %s", nextCommand));
        return nextCommand;
    }

    @Override
    public ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException {
        calls.append(format("userHasEntered(%s)", user));
        return null;
    }

    @Override
    public ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException {
        calls.append(format("userHasExited(%s)", user));
        return null;
    }

    @Override
    public ElevatorEngine reset(String cause) {
        calls.append(format("reset(%s)", cause));
        return null;
    }

    static String calls() {
        return calls.toString();
    }

    static void clearCalls() {
        if (calls.length() > 0) {
            calls.delete(0, calls.length());
        }
    }
}
