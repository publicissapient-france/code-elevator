package elevator.client.assertions;

import elevator.client.Clock;
import elevator.client.Direction;
import elevator.client.Elevator;
import elevator.client.ElevatorState;
import org.fest.assertions.GenericAssert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

public class ElevatorAssert extends GenericAssert<ElevatorAssert, Elevator> {

    private static final Pattern PATTERN = Pattern.compile("(OPEN|CLOSE)?(?: )*(\\d+)?");
    private Integer currentStage;
    private ElevatorState currentState;

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

    public ElevatorAssert call(Integer atStage, Direction to) {
        actual.call(atStage, to);
        return this;
    }

    public ElevatorAssert go(Integer stageToGo) {
        actual.go(stageToGo);
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
        String expectedElevatorStage = matcher.group(2);

        if (expectedElevatorState != null) {
            currentState = ElevatorState.valueOf(expectedElevatorState);
        }
        if (expectedElevatorStage != null) {
            currentStage = parseInt(expectedElevatorStage);
        }

        if (currentState != null) {
            assertThat(actual.state()).isEqualTo(currentState);
        }
        if (currentStage != null) {
            assertThat(actual.stage()).isEqualTo(currentStage);
        }
    }
}
