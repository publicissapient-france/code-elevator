package elevator;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static java.lang.Math.random;

public class User {

    private final Elevator elevator;
    private final Integer floor;
    private final Integer floorToGo;

    private User.State state;

    public User(Elevator elevator) {
        this.elevator = elevator;
        this.state = State.WAITING;

        Direction direction;
        if (random() > .5) {
            floor = new Double(random() * 5).intValue();
            direction = random() > .5 ? UP : DOWN;
            floorToGo = direction == UP ? 5 : 0;
        } else {
            floor = 0;
            direction = UP;
            floorToGo = new Double(random() * 5).intValue();
        }

        elevator.call(floor, direction);
    }

    public User elevatorIsOpen(Integer atFloor) {
        if (state == State.WAITING && atFloor.equals(floor)) {
            elevator.go(floorToGo);
            state = State.TRAVELLING;
            return this;
        }
        if (state == State.TRAVELLING && atFloor.equals(floorToGo)) {
            state = State.DONE;
        }
        return this;
    }

    private enum State {
        WAITING, TRAVELLING, DONE,;
    }

}
