package elevator.user.assertions;

import elevator.Direction;
import elevator.user.InitializationStrategy;

import static org.fest.assertions.Assertions.assertThat;

public class InitializationStrategyAssert {

    private final InitializationStrategy actual;

    public InitializationStrategyAssert(InitializationStrategy actual) {
        this.actual = actual;
    }

    public InitializationStrategyAssert initialFloor(Integer expectedFloor) {
        assertThat(expectedFloor).as("initial floor").isEqualTo(actual.initialFloor());
        return this;
    }

    public InitializationStrategyAssert initialDirection(Direction expectedDirection) {
        assertThat(expectedDirection).as("initial direction").isEqualTo(actual.initialDirection());
        return this;
    }

    public InitializationStrategyAssert floorToGo(Integer expectedFloorToGo) {
        assertThat(expectedFloorToGo).as("floor to go").isEqualTo(actual.floorToGo());
        return this;
    }

}
