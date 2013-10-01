package elevator.engine.naive;

import elevator.Command;
import elevator.Direction;
import elevator.user.User;
import elevator.engine.ElevatorEngine;

import static elevator.Command.*;

public class NaiveElevator implements ElevatorEngine {

    private Integer floor = LOWER_FLOOR;
    private Direction direction = Direction.UP;
    private State nextState = State.OPEN;

    private enum State {
        OPEN, CLOSE, MOVE,;
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
        switch (nextState) {
            case OPEN:
                nextState = State.CLOSE;
                return OPEN;
            case CLOSE:
                nextState = State.MOVE;
                return CLOSE;
            case MOVE:
                nextState = State.OPEN;
                if (Direction.UP.equals(direction)) {
                    return goesUp();
                } else {
                    return goesDown();
                }
            default:
                return NOTHING;
        }
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        return this;
    }

    @Override
    public ElevatorEngine reset(String cause) {
        floor = LOWER_FLOOR;
        direction = Direction.UP;
        nextState = State.OPEN;
        return this;
    }

    private Command goesUp() {
        floor++;
        if (HIGHER_FLOOR.equals(floor)) {
            direction = Direction.DOWN;
        }
        return UP;
    }

    private Command goesDown() {
        floor--;
        if (LOWER_FLOOR.equals(floor)) {
            direction = Direction.UP;
        }
        return DOWN;
    }

}
