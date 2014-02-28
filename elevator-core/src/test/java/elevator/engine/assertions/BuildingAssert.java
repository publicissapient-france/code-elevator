package elevator.engine.assertions;

import elevator.Building;
import elevator.Door;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.IterableAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildingAssert extends AbstractObjectAssert<BuildingAssert, Building> {
    public BuildingAssert(Building actual) {
        super(actual, BuildingAssert.class);
    }

    public IterableAssert users() {
        return assertThat(actual.users()).as("building users (delivered users are gone)");
    }

    public BuildingAssert doorIs(Door door) {
        assertThat(actual.door()).isEqualTo(door);
        return this;
    }

    public BuildingAssert floorIs(Integer expectedFloor) {
        assertThat(actual.floor()).isEqualTo(expectedFloor);
        return this;
    }
}
