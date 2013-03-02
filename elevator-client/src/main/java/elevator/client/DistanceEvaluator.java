package elevator.client;

import static elevator.client.Direction.UP;

public class DistanceEvaluator {

    private final Command reference;
    private final Integer maxFloor;
    private final Integer numberOfFloors;

    DistanceEvaluator(Command reference, Integer minFloor, Integer maxFloor) {
        this.reference = reference;
        this.maxFloor = maxFloor;
        this.numberOfFloors = maxFloor - minFloor;
    }

    public Integer getDistance(Command command) {
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
            return numberOfFloors + (maxFloor - command.floor);
        }
    }
}