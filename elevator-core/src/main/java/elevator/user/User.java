package elevator.user;

import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;

public class User {

    private final ElevatorEngine elevatorEngine;
    private final Integer initialFloor;
    private final Direction initialDirection;
    private final Integer floorToGo;
    private Integer currentFloor;
    private Integer tickToGo;
    private User.State state;
    private Integer tickToWait;

    public User(ElevatorEngine elevatorEngine, InitializationStrategy strategy) throws ElevatorIsBrokenException {
        this.elevatorEngine = elevatorEngine;
        this.state = State.WAITING;
        this.tickToGo = 0;
        this.tickToWait = 0;
        this.initialFloor = strategy.initialFloor();
        this.initialDirection = strategy.initialDirection();
        this.floorToGo = strategy.floorToGo();
        this.currentFloor = initialFloor;

        elevatorEngine.call(initialFloor, initialDirection);
    }

    public void elevatorIsOpen(Integer floor) throws ElevatorIsBrokenException {
        if (waiting() && at(floor)) {
            elevatorEngine.userHasEntered(this);
            elevatorEngine.go(floorToGo);
            state = State.TRAVELLING;
        } else if (traveling() && at(floorToGo)) {
            elevatorEngine.userHasExited(this);
            state = State.DONE;
        }
    }

    public void elevatorIsAt(Integer currentFloor) {
        if (traveling()) {
            this.currentFloor = currentFloor;
        }
    }

    public Boolean waiting() {
        return state == State.WAITING;
    }

    public Boolean traveling() {
        return state == State.TRAVELLING;
    }

    public Boolean done() {
        return state == State.DONE;
    }

    public Boolean at(int floor) {
        return this.currentFloor == floor;
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

    public void tick() {
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

    private enum State {
        WAITING, TRAVELLING, DONE,;
    }

    @Override
    public String toString() {
        StringBuilder userToString = new StringBuilder("User ");
        switch (state) {
            case WAITING:
                userToString.append("waiting at ").append(initialFloor).
                        append(" since ").append(tickToWait).append(" tick").append((tickToWait > 0) ? "s" : "");
                break;
            case TRAVELLING:
                userToString.append("traveling from ").append(initialFloor).
                        append(" to ").append(floorToGo);
                break;
            case DONE:
                userToString.append("arrived at ").append(floorToGo).
                        append(" with ").append(tickToWait + tickToGo).append(" ticks");
                break;
        }
        return userToString.toString();
    }

}
