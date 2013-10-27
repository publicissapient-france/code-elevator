package elevator.user;

import java.util.Random;

import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Math.abs;
import static java.util.Arrays.asList;

public class RandomUser implements InitializationStrategy {

    private final Random random;
    private final Integer numberOfFloors;

    public RandomUser() {
        this(new Random());
    }

    RandomUser(Random random) {
        this.random = random;
        this.numberOfFloors = abs(HIGHER_FLOOR - LOWER_FLOOR) + 1;
    }

    @Override
    public FloorsAndDirection create() {
        final Integer initialFloor;
        final Integer floorToGo;
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
        return new FloorsAndDirection(initialFloor, floorToGo);
    }

    private Integer randomFloorExcept(Integer... exceptions) {
        final Integer randomFloor = random.nextInt(numberOfFloors) + LOWER_FLOOR;
        if (asList(exceptions).contains(randomFloor)) {
            return randomFloorExcept(exceptions);
        }
        return randomFloor;
    }

}
