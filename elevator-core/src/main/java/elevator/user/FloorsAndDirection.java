package elevator.user;

import elevator.Direction;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;

public class FloorsAndDirection {

    final Integer initialFloor;

    final Direction initialDirection;

    final Integer floorToGo;

    public FloorsAndDirection(Integer initialFloor, Integer floorToGo) {
        this.initialFloor = initialFloor;
        this.initialDirection = initialFloor < floorToGo ? UP : DOWN;
        this.floorToGo = floorToGo;
    }

}
