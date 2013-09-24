package elevator;

import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.max;
import static java.lang.Math.random;

public class User {

    private final ElevatorEngine elevatorEngine;
    private final Integer initialFloor;
    private final Direction initialDirection;
    private final Integer floorToGo;
    private Integer currentFloor;
    private Integer tickToGo;
    private User.State state;
    private Integer tickToWait;

    User(ElevatorEngine elevatorEngine) throws ElevatorIsBrokenException {
        this.elevatorEngine = elevatorEngine;
        this.state = State.WAITING;
        this.tickToGo = 0;
        this.tickToWait = 0;

        Direction direction;
        if (randomBoolean()) {
            initialFloor = randomFloor();
            direction = randomDirection();
            if (LOWER_FLOOR.equals(initialFloor)) {
                direction = UP;
            }
            if (HIGHER_FLOOR.equals(initialFloor)) {
                direction = DOWN;
            }
            floorToGo = direction == UP ? HIGHER_FLOOR : LOWER_FLOOR;
        } else {
            initialFloor = LOWER_FLOOR;
            direction = UP;
            floorToGo = max(randomFloor(), LOWER_FLOOR + 1);
        }
        currentFloor = initialFloor;
        initialDirection = direction;

        elevatorEngine.call(initialFloor, direction);
    }

    void elevatorIsOpen(Integer floor) throws ElevatorIsBrokenException {
        if (waiting() && at(floor)) {
            elevatorEngine.userHasEntered(this);
            elevatorEngine.go(floorToGo);
            state = State.TRAVELLING;
        } else if (traveling() && at(floorToGo)) {
            elevatorEngine.userHasExited(this);
            state = State.DONE;
        }
    }

    boolean waiting() {
        return state == State.WAITING;
    }

    Boolean traveling() {
        return state == State.TRAVELLING;
    }

    Boolean done() {
        return state == State.DONE;
    }

    Boolean at(int floor) {
        return this.currentFloor == floor;
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

    public Integer getTickToGo() {
        return tickToGo;
    }

    public Integer getInitialFloor() {
        return initialFloor;
    }

    public Direction getInitialDirection() {
        return initialDirection;
    }

    public Integer getFloorToGo() {
        return floorToGo;
    }

    void tick() {
        if (traveling()) {
            tickToGo++;
        }
        if (waiting()) {
            tickToWait++;
        }
    }

    public Integer getTickToWait() {
        return tickToWait;
    }

    void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

    private enum State {
        WAITING, TRAVELLING, DONE,;
    }

}
