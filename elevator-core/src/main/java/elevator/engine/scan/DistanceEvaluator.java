package elevator.engine.scan;

import static elevator.Direction.UP;

class DistanceEvaluator {

    private final Command reference;
    private final Integer higherFloor;
    private final Integer numberOfFloors;

    DistanceEvaluator(Command reference, Integer lowerFloor, Integer higherFloor) {
        this.reference = reference;
        this.higherFloor = higherFloor;
        this.numberOfFloors = higherFloor - lowerFloor;
    }

    Integer getDistance(Command command) {
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