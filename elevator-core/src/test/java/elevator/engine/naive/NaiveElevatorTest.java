package elevator.engine.naive;

import org.junit.Test;

import static elevator.Direction.UP;
import static elevator.engine.assertions.Assertions.assertThat;

public class NaiveElevatorTest {

    @Test
    public void should_open_and_close_at_each_floor() {
        NaiveElevator naiveElevator = new NaiveElevator();

        assertThat(naiveElevator).is("CLOSE 0").call(3, UP).
                onTick("      1").call(2, UP).
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      2").
                onTick("OPEN   ").
                onTick("CLOSE  ").go(4).
                onTick("      3").
                onTick("OPEN   ").
                onTick("CLOSE  ").go(5).
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      5").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      4");
    }

}
