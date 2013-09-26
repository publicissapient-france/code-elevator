package elevator.engine.assertions;

import elevator.Command;
import elevator.Direction;
import elevator.Door;
import elevator.engine.ElevatorEngine;
import org.fest.assertions.GenericAssert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static elevator.Door.CLOSE;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

public class ElevatorEngineAssert extends GenericAssert<ElevatorEngineAssert, ElevatorEngine> {

    private static final Pattern PATTERN = Pattern.compile("(OPEN|CLOSE)?(?: )*(\\d+)?");

    private final StringBuilder expectedStates;

    private Integer expectedFloor;
    private Door expectedDoor;
    private Integer actualFloor = LOWER_FLOOR;
    private Door actualDoor = CLOSE;

    ElevatorEngineAssert(ElevatorEngine actual) {
        super(ElevatorEngineAssert.class, actual);
        expectedStates = new StringBuilder();
    }

    public ElevatorEngineAssert is(String expectedState) {
        assertState(getMatcher(expectedState));
        return this;
    }

    public ElevatorEngineAssert call(Integer atFloor, Direction to) {
        actual.call(atFloor, to);
        return this;
    }

    public ElevatorEngineAssert go(Integer floorToGo) {
        actual.go(floorToGo);
        return this;
    }

    public ElevatorEngineAssert reset(String cause) {
        actual.reset(cause);
        return this;
    }

    private Matcher getMatcher(String expectedState) {
        Matcher matcher = PATTERN.matcher(expectedState);

        if (!matcher.matches()) {
            throw fail(format("\"%s\" is not recognized as a state expression", expectedState));
        }

        expectedStates.append('\n');
        expectedStates.append(expectedState);
        return matcher;
    }

    private void assertState(Matcher matcher) {
        String expectedElevatorState = matcher.group(1);
        String expectedElevatorFloor = matcher.group(2);

        if (expectedElevatorState != null) {
            expectedDoor = Door.valueOf(expectedElevatorState);
        }
        if (expectedElevatorFloor != null) {
            expectedFloor = parseInt(expectedElevatorFloor);
        }

        if (expectedDoor != null) {
            assertThat(actualDoor).as(expectedStates.toString()).isEqualTo(expectedDoor);
        }
        if (expectedFloor != null) {
            assertThat(actualFloor).as(expectedStates.toString()).isEqualTo(expectedFloor);
        }
    }

    public ElevatorEngineAssert onTick(String expectedState) {
        Matcher matcher = getMatcher(expectedState);
        tick();
        assertState(matcher);
        return this;
    }

    public ElevatorEngineAssert tick() {
        Command command = actual.nextCommand();

        switch (command) {
            case CLOSE:
                actualDoor = CLOSE;
                break;
            case OPEN:
                actualDoor = OPEN;
                break;
            case UP:
                actualDoor = CLOSE;
                actualFloor++;
                break;
            case DOWN:
                actualDoor = CLOSE;
                actualFloor--;
                break;
            case NOTHING:
                break;
        }

        return this;
    }

}
