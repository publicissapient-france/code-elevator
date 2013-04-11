package elevator.engine.assertions;

import elevator.Building;
import elevator.engine.ElevatorEngine;

public class Assertions {

    public static ElevatorEngineAssert assertThat(ElevatorEngine actual) {
        return new ElevatorEngineAssert(actual);
    }

    public static BuildingAssert assertThat(Building actual) {
        return new BuildingAssert(actual);
    }

}
