package elevator.client.assertions;

import elevator.Direction;
import elevator.Door;
import elevator.client.Clock;
import elevator.client.Elevator;
import org.fest.assertions.GenericAssert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

public class ElevatorAssert extends GenericAssert<ElevatorAssert, Elevator> {

    private static final Pattern PATTERN = Pattern.compile("(OPEN|CLOSE)?(?: )*(\\d+)?");

    private Integer currentFloor;
    private Door currentState;

    ElevatorAssert(final Elevator actual) {
        super(ElevatorAssert.class, actual);
    }

    public ElevatorWithClockAssert with(Clock clock) {
        return new ElevatorWithClockAssert(actual, clock);
    }

    public ElevatorAssert is(final String expectedState) {
        assertState(getMatcher(expectedState));
        return this;
    }

    public ElevatorAssert call(Integer atFloor, Direction to) {
        actual.call(atFloor, to);
        return this;
    }

    public ElevatorAssert go(Integer floorToGo) {
        actual.go(floorToGo);
        return this;
    }

    protected Matcher getMatcher(String expectedState) {
        Matcher matcher = PATTERN.matcher(expectedState);

        if (!matcher.matches()) {
            throw fail(format("\"%s\" is not recognized as a state expression", expectedState));
        }
        return matcher;
    }

    protected void assertState(Matcher matcher) {
        String expectedElevatorState = matcher.group(1);
        String expectedElevatorFloor = matcher.group(2);

        if (expectedElevatorState != null) {
            currentState = Door.valueOf(expectedElevatorState);
        }
        if (expectedElevatorFloor != null) {
            currentFloor = parseInt(expectedElevatorFloor);
        }

        if (currentState != null) {
            assertThat(actual.door()).isEqualTo(currentState);
        }
        if (currentFloor != null) {
            assertThat(actual.floor()).isEqualTo(currentFloor);
        }
    }

}
