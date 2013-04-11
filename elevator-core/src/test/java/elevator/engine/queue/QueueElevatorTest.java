package elevator.engine.queue;

import elevator.engine.ElevatorEngine;
import elevator.engine.naive.NaiveElevator;
import org.junit.Test;

import static elevator.Direction.UP;
import static elevator.engine.assertions.Assertions.assertThat;

public class QueueElevatorTest {

    @Test
    public void should_satisfy_each_demand_one_by_one() {
        ElevatorEngine elevator = new QueueElevator();

        assertThat(elevator).is("CLOSE 0").call(3, UP).
                onTick("      1").call(2, UP).
                onTick("      2").
                onTick("      3").
                onTick("OPEN   ").
                onTick("CLOSE  ").go(5).
                onTick("      2").
                onTick("OPEN   ").
                onTick("CLOSE  ").go(4).
                onTick("      3").
                onTick("      4").
                onTick("      5").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }


}
