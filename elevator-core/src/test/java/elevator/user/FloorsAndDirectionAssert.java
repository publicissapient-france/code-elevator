package elevator.user;

import elevator.Direction;

import static org.assertj.core.api.Assertions.assertThat;

class FloorsAndDirectionAssert {
    private final FloorsAndDirection actual;

    FloorsAndDirectionAssert(FloorsAndDirection actual) {
        this.actual = actual;
    }

    FloorsAndDirectionAssert initialFloor(Integer expectedFloor) {
        assertThat(expectedFloor).as("initial floor").isEqualTo(actual.initialFloor);
        return this;
    }

    FloorsAndDirectionAssert initialDirection(Direction expectedDirection) {
        assertThat(expectedDirection).as("initial direction").isEqualTo(actual.initialDirection);
        return this;
    }

    FloorsAndDirectionAssert floorToGo(Integer expectedFloorToGo) {
        assertThat(expectedFloorToGo).as("floor to go").isEqualTo(actual.floorToGo);
        return this;
    }
}
