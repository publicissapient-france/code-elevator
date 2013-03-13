package elevator.assertions;

import elevator.ElevatorEngine;

public class Assertions {

    public static ElevatorEngineAssert assertThat(ElevatorEngine actual) {
        return new ElevatorEngineAssert(actual);
    }

}
