package elevator;

import elevator.engine.ElevatorEngine;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.random;

public class User implements ClockListener {

    private final ElevatorEngine elevatorEngine;
    private final Integer floor;
    private final Integer floorToGo;
    private Integer tickToGo;

    private User.State state;

    public User(ElevatorEngine elevatorEngine) {
        this.elevatorEngine = elevatorEngine;
        this.state = State.WAITING;
        this.tickToGo = 0;

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


    public User elevatorIsOpen(Integer floor) {
        if (waiting() && at(floor)) {
            elevatorEngine.userHasEntered(this);
            elevatorEngine.go(floorToGo);
            state = State.TRAVELLING;
            return this;
        }
        if (traveling() && at(floorToGo)) {
            elevatorEngine.userHasExited(this);
            state = State.DONE;
        }
        return this;
    }

    public boolean waiting() {
        return state == State.WAITING;
    }

    public Boolean traveling() {
        return state == State.TRAVELLING;
    }

    public Boolean done() {
        return state == State.DONE;
    }

    public Boolean at(int floor) {
        return this.floor == floor;
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

    public Integer getFloor() {
        return floor;
    }

    public Integer getFloorToGo() {
        return floorToGo;
    }

    @Override
    public ClockListener onTick() {
        if (traveling()) {
            tickToGo++;
        }
        return this;
    }

    private enum State {
        WAITING, TRAVELLING, DONE,;
    }

}
