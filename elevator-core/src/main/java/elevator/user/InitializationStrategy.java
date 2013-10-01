package elevator.user;

import elevator.Direction;

public interface InitializationStrategy {

    Integer initialFloor();

    Direction initialDirection();

    Integer floorToGo();

}
