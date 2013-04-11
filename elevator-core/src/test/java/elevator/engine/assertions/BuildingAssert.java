package elevator.engine.assertions;

import elevator.Building;
import elevator.Door;
import org.fest.assertions.CollectionAssert;
import org.fest.assertions.GenericAssert;

import static org.fest.assertions.Assertions.assertThat;

public class BuildingAssert extends GenericAssert<BuildingAssert, Building> {

    public BuildingAssert(Building actual) {
        super(BuildingAssert.class, actual);
    }

    public CollectionAssert users() {
        return assertThat(actual.users());
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
