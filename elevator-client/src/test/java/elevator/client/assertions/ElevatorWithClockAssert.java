package elevator.client.assertions;

import elevator.client.Clock;
import elevator.client.Direction;
import elevator.client.Elevator;

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

    public ElevatorWithClockAssert call(Integer atFloor, Direction to) {
        super.call(atFloor, to);
        return this;
    }

    public ElevatorWithClockAssert go(Integer floorToGo) {
        super.go(floorToGo);
        return this;
    }

}
