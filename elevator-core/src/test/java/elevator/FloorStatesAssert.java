package elevator;

import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class FloorStatesAssert {
    private final SortedSet<FloorState> actual;

    FloorStatesAssert(Set<FloorState> actual) {
        this.actual = new TreeSet<>((floorState1, floorState2) -> Integer.compare(floorState1.floor, floorState2.floor));
        this.actual.addAll(actual);
    }

    FloorStatesAssert hasSize(Integer expectedSize) {
        assertThat(actual).hasSize(expectedSize);
        return this;
    }

    FloorStatesAssert floors(Integer... expectedFloors) {
        Integer[] actualFloors = actual.stream()
                .map(floorState -> floorState.floor)
                .collect(toCollection(LinkedList::new))
                .toArray(new Integer[actual.size()]);
        assertThat(actualFloors).as("floors").isEqualTo(expectedFloors);
        return this;
    }

    FloorStatesAssert waitingUsers(long... expectedWaitingUsers) {
        long[] actualWaitingUsers = actual.stream()
                .mapToLong(floorState -> floorState.waitingUsers)
                .toArray();
        assertThat(actualWaitingUsers).as("waiting users").isEqualTo(expectedWaitingUsers);
        return this;
    }

    FloorStatesAssert up(Boolean... expectedUp) {
        Boolean[] actualUp = actual.stream()
                .map(floorState -> floorState.up)
                .collect(toCollection(LinkedList::new))
                .toArray(new Boolean[actual.size()]);
        assertThat(actualUp).as("up").isEqualTo(expectedUp);
        return this;
    }

    FloorStatesAssert down(Boolean... expectedDown) {
        Boolean[] actualDown = actual.stream()
                .map(floorState -> floorState.down)
                .collect(toCollection(LinkedList::new))
                .toArray(new Boolean[actual.size()]);
        assertThat(actualDown).as("down").isEqualTo(expectedDown);
        return this;
    }

    FloorStatesAssert target(Boolean... expectedTarget) {
        Boolean[] actualTarget = actual.stream()
                .map(floorState -> floorState.target)
                .collect(toCollection(LinkedList::new))
                .toArray(new Boolean[actual.size()]);
        assertThat(actualTarget).as("target").isEqualTo(expectedTarget);
        return this;
    }
}
