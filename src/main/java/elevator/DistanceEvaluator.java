package elevator;

import static elevator.Direction.UP;

public class DistanceEvaluator {

    private final Command reference;
    private final Integer maxStage;
    private final Integer numberOfStages;

    DistanceEvaluator(Command reference, Integer minStage, Integer maxStage) {
        this.reference = reference;
        this.maxStage = maxStage;
        this.numberOfStages = maxStage - minStage;
    }

    public Integer getDistance(Command command) {
        Integer firstIndex = positionIndex(reference);
        Integer secondIndex = positionIndex(command);

        if (firstIndex > secondIndex) {
            secondIndex += numberOfStages * 2;
        }

        return secondIndex - firstIndex;
    }

    private Integer positionIndex(Command command) {
        if (command.direction == UP) {
            return command.stage;
        } else {
            return numberOfStages + (maxStage - command.stage);
        }
    }
}