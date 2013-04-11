package elevator;

import elevator.engine.ElevatorEngine;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.random;

public class User {

    private final ElevatorEngine elevatorEngine;
    private final Integer floor;
    private final Integer floorToGo;

    private User.State state;

    public User(ElevatorEngine elevatorEngine) {
        this.elevatorEngine = elevatorEngine;
        this.state = State.WAITING;

        Direction direction;
        if (randomBoolean()) {
            floor = randomFloor();
            direction = randomDirection();
            floorToGo = direction == UP ? HIGHER_FLOOR : LOWER_FLOOR;
        } else {
            floor = LOWER_FLOOR;
            direction = UP;
            floorToGo = randomFloor();
        }

        elevatorEngine.call(floor, direction);
    }

    public User elevatorIsOpen(Integer atFloor) {
        if (state == State.WAITING && atFloor.equals(floor)) {
            elevatorEngine.go(floorToGo);
            state = State.TRAVELLING;
            return this;
        }
        if (state == State.TRAVELLING && atFloor.equals(floorToGo)) {
            state = State.DONE;
        }
        return this;
    }

    private Integer randomFloor() {
        return new Double(random() * HIGHER_FLOOR).intValue();
    }

    private Direction randomDirection() {
        return randomBoolean() ? UP : DOWN;
    }

    private Boolean randomBoolean() {
        return random() > .5;
    }

    private enum State {
        WAITING, TRAVELLING, DONE, REJECTED,;
    }

}
