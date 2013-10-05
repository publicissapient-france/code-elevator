package elevator.engine.scan;

import static elevator.engine.scan.ElevatorDirection.NONE;
import static elevator.engine.scan.ElevatorDirection.UP;

public class DistanceEvaluator {

    private final Command reference;
    private final Integer higherFloor;
    private final Integer numberOfFloors;

    DistanceEvaluator(Command reference, Integer lowerFloor, Integer higherFloor) {
        this.reference = reference;
        this.higherFloor = higherFloor;
        this.numberOfFloors = higherFloor - lowerFloor;
    }

    public Integer getDistance(Command command) {
        if (reference.direction == NONE || command.direction == NONE) {
            return Math.abs(reference.floor - command.floor);
        }

        Integer firstIndex = positionIndex(reference);
        Integer secondIndex = positionIndex(command);

        if (firstIndex > secondIndex) {
            secondIndex += numberOfFloors * 2;
        }

        return secondIndex - firstIndex;
    }

    private Integer positionIndex(Command command) {
        if (command.direction == UP) {
            return command.floor;
        } else {
            return numberOfFloors + (higherFloor - command.floor);
        }
    }

}