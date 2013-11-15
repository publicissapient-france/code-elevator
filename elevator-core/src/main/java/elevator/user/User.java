package elevator.user;

import elevator.Direction;
import elevator.Elevator;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;

public class User {

    private final ElevatorEngine elevatorEngine;
    private final FloorsAndDirection initialState;
    private Integer currentFloor;
    private Integer tickToGo;
    private User.State state;
    private Integer tickToWait;

    public User(ElevatorEngine elevatorEngine, FloorsAndDirection floorsAndDirection) throws ElevatorIsBrokenException {
        this.elevatorEngine = elevatorEngine;
        this.state = State.WAITING;
        this.tickToGo = 0;
        this.tickToWait = 0;
        this.initialState = floorsAndDirection;
        this.currentFloor = floorsAndDirection.initialFloor;

        elevatorEngine.call(floorsAndDirection.initialFloor, floorsAndDirection.initialDirection);
    }

    public void elevatorIsOpen(Elevator elevator, Integer floor) throws ElevatorIsBrokenException {
        if (waiting() && at(floor)) {
            elevator.userHasEntered(this);
            elevatorEngine.userHasEntered(this);
            elevatorEngine.go(initialState.floorToGo);
            state = State.TRAVELLING;
        } else if (traveling() && at(initialState.floorToGo)) {
            elevator.userHasExited(this);
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
        return initialState.initialFloor;
    }

    public Direction getInitialDirection() {
        return initialState.initialDirection;
    }

    public Integer getFloorToGo() {
        return initialState.floorToGo;
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
                userToString.append("waiting at ").append(initialState.initialFloor).
                        append(" since ").append(tickToWait).append(" tick").append((tickToWait > 0) ? "s" : "");
                break;
            case TRAVELLING:
                userToString.append("traveling from ").append(initialState.initialFloor).
                        append(" to ").append(initialState.floorToGo);
                break;
            case DONE:
                userToString.append("arrived at ").append(initialState.floorToGo).
                        append(" with ").append(tickToWait + tickToGo).append(" ticks");
                break;
        }
        return userToString.toString();
    }

}
