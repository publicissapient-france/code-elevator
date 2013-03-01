package elevator.assertions;

import elevator.Clock;
import elevator.Direction;
import elevator.Elevator;

import java.util.regex.Matcher;

public class ElevatorWithClockAssert extends ElevatorAssert {

    private Clock clock;

    protected ElevatorWithClockAssert(Elevator actual, Clock clock) {
        super(actual);
        this.clock = clock;
    }

    public ElevatorWithClockAssert onTick(final String expectedState) {
        Matcher matcher = getMatcher(expectedState);
        clock.tick();
        assertState(matcher);
        return this;
    }

    public ElevatorWithClockAssert tick() {
        clock.tick();
        return this;
    }

    public ElevatorWithClockAssert call(Integer atStage, Direction to) {
        super.call(atStage, to);
        return this;
    }

    public ElevatorWithClockAssert go(Integer toStage) {
        super.go(toStage);
        return this;
    }

}
