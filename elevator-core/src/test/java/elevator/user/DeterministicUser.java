package elevator.user;

import elevator.Direction;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;

public class DeterministicUser implements InitializationStrategy {

    private final Integer initialFloor;
    private final Integer floorToGo;

    public DeterministicUser(Integer initialFloor, Integer floorToGo) {
        this.initialFloor = initialFloor;
        this.floorToGo = floorToGo;
    }

    @Override
    public Integer initialFloor() {
        return initialFloor;
    }

    @Override
    public Direction initialDirection() {
        return initialFloor < floorToGo ? UP : DOWN;
    }

    @Override
    public Integer floorToGo() {
        return floorToGo;
    }

}
