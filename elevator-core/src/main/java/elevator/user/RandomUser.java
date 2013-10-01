package elevator.user;

import elevator.Direction;
import elevator.user.User;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.max;
import static java.lang.Math.random;

public class RandomUser implements InitializationStrategy {

    private final Integer initialFloor;
    private final Integer floorToGo;
    private final Direction initialDirection;

    public RandomUser() {
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
        initialDirection = direction;
    }

    @Override
    public Integer initialFloor() {
        return initialFloor;
    }

    @Override
    public Direction initialDirection() {
        return initialDirection;
    }

    @Override
    public Integer floorToGo() {
        return floorToGo;
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

}
