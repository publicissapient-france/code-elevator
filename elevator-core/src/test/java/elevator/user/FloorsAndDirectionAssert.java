package elevator.user;

import elevator.Direction;

import static org.assertj.core.api.Assertions.assertThat;

class FloorsAndDirectionAssert {
    private final FloorsAndDirection actual;

    FloorsAndDirectionAssert(FloorsAndDirection actual) {
        this.actual = actual;
    }

    FloorsAndDirectionAssert initialFloor(Integer expectedFloor) {
        assertThat(actual.initialFloor).as("initial floor").isEqualTo(expectedFloor);
        return this;
    }

    FloorsAndDirectionAssert initialDirection(Direction expectedDirection) {
        assertThat(actual.initialDirection).as("initial direction").isEqualTo(expectedDirection);
        return this;
    }

    FloorsAndDirectionAssert floorToGo(Integer expectedFloorToGo) {
        assertThat(actual.floorToGo).as("floor to go").isEqualTo(expectedFloorToGo);
        return this;
    }
}
