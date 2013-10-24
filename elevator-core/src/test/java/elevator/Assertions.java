package elevator;

import java.util.Set;

class Assertions {

    static FloorStatesAssert assertThat(Set<FloorState> actual) {
        return new FloorStatesAssert(actual);
    }

}
