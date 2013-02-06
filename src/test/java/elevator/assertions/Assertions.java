package elevator.assertions;

import elevator.Elevator;

public class Assertions {

    public static ElevatorAssert assertThat(final Elevator actual) {
        return new ElevatorAssert(actual);
    }

}
