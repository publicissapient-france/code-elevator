package elevator.user;

import elevator.Direction;

import java.util.Random;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.abs;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class RandomUser implements InitializationStrategy {

    private final Random random;
    private final Integer numberOfFloors;
    private final Integer initialFloor;
    private final Integer floorToGo;
    private final Direction initialDirection;

    public RandomUser() {
        this(new Random());
    }

    RandomUser(Random random) {
        this.random = random;
        this.numberOfFloors = abs(HIGHER_FLOOR - LOWER_FLOOR) + ((LOWER_FLOOR <= 0 && 0 <= HIGHER_FLOOR) ? 1 : 0);
        if (random.nextBoolean()) {
            initialFloor = randomFloorExcept(0);
            if (random.nextBoolean()) {
                floorToGo = randomFloorExcept(0, initialFloor);
            } else {
                floorToGo = 0;
            }
        } else {
            initialFloor = 0;
            floorToGo = randomFloorExcept(0);
        }
        initialDirection = initialFloor < floorToGo ? UP : DOWN;
    }

    private Integer randomFloorExcept(Integer... exceptions) {
        final Integer randomFloor = random.nextInt(numberOfFloors) - LOWER_FLOOR;
        if (asList(exceptions).contains(randomFloor)) {
            return randomFloorExcept(exceptions);
        }
        return randomFloor;
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

    @Override
    public String toString() {
        return format("user from floor %d to %d %s", initialFloor, floorToGo, initialDirection);
    }

}
