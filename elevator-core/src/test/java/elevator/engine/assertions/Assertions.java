package elevator.engine.assertions;

import elevator.engine.ElevatorEngine;

public class Assertions {

    public static ElevatorEngineAssert assertThat(ElevatorEngine actual) {
        return new ElevatorEngineAssert(actual);
    }

}
