package elevator;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

class FloorStatesAssert {
    private final SortedSet<FloorState> actual;

    FloorStatesAssert(Set<FloorState> actual) {
        this.actual = new TreeSet<>(new Comparator<FloorState>() {
            @Override
            public int compare(FloorState floorState1, FloorState floorState2) {
                return Integer.compare(floorState1.floor, floorState2.floor);
            }
        });
        this.actual.addAll(actual);
    }

    FloorStatesAssert hasSize(Integer expectedSize) {
        assertThat(actual).hasSize(expectedSize);
        return this;
    }

    FloorStatesAssert floors(Integer... expectedFloors) {
        Integer[] actualFloors = new Integer[actual.size()];
        Integer i = 0;
        for (FloorState floorState : actual) {
            actualFloors[i++] = floorState.floor;
        }
        assertThat(actualFloors).as("floors").isEqualTo(expectedFloors);
        return this;
    }

    FloorStatesAssert waitingUsers(Integer... expectedWaitingUsers) {
        Integer[] actualWaitingUsers = new Integer[actual.size()];
        Integer i = 0;
        for (FloorState floorState : actual) {
            actualWaitingUsers[i++] = floorState.waitingUsers;
        }
        assertThat(actualWaitingUsers).as("waiting users").isEqualTo(expectedWaitingUsers);
        return this;
    }

    FloorStatesAssert up(Boolean... expectedUp) {
        Boolean[] actualUp = new Boolean[actual.size()];
        Integer i = 0;
        for (FloorState floorState : actual) {
            actualUp[i++] = floorState.up;
        }
        assertThat(actualUp).as("up").isEqualTo(expectedUp);
        return this;
    }

    FloorStatesAssert down(Boolean... expectedDown) {
        Boolean[] actualDown = new Boolean[actual.size()];
        Integer i = 0;
        for (FloorState floorState : actual) {
            actualDown[i++] = floorState.down;
        }
        assertThat(actualDown).as("down").isEqualTo(expectedDown);
        return this;
    }

    FloorStatesAssert target(Boolean... expectedTargets) {
        Boolean[] actualTarget = new Boolean[actual.size()];
        Integer i = 0;
        for (FloorState floorState : actual) {
            actualTarget[i++] = floorState.target;
        }
        assertThat(actualTarget).as("target").isEqualTo(expectedTargets);
        return this;
    }
}
