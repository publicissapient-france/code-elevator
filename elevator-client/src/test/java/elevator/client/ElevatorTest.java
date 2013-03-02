package elevator.client;

import org.junit.Before;
import org.junit.Test;

import static elevator.client.Direction.UP;
import static elevator.client.assertions.Assertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;

public class ElevatorTest {

    private Elevator elevator;
    private Clock clock;

    @Before
    public void createElevator() {
        clock = new Clock();
        elevator = new Elevator();
        clock.addObserver(elevator);
    }

    @Test
    public void should_print_pretty_string() {
        assertThat(elevator.toString()).isEqualTo("elevator CLOSE 0");
    }

    @Test
    public void should_start_closed_at_floor_0() {
        assertThat(elevator).is("CLOSE 0");
    }

    @Test
    public void should_does_nothing_when_nobody_call_elevator() {
        assertThat(elevator).with(clock).
                onTick("CLOSE 0");
    }

    @Test
    public void should_open_doors() {
        assertThat(elevator).with(clock).call(0, UP).
                onTick("OPEN 0");
    }

    @Test
    public void should_close_doors_after_call() {
        assertThat(elevator).with(clock).call(0, UP).
                onTick("OPEN  0").
                onTick("CLOSE  ");
    }

    @Test
    public void should_call_elevator_and_open_doors() {
        assertThat(elevator).with(clock).call(4, UP).
                onTick("      1").
                onTick("      2").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_go_to_stage() {
        assertThat(elevator).with(clock).call(0, UP).tick().go(4).
                onTick("CLOSE 0").
                onTick("      1").
                onTick("      2").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_stop_at_two_stages() {
        assertThat(elevator).with(clock).call(0, UP).tick().go(4).go(2).
                onTick("CLOSE 0").
                onTick("      1").
                onTick("      2").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_go_at_same_stage_for_any_times() {
        assertThat(elevator).with(clock).call(0, UP).tick().go(4).
                onTick("CLOSE 0").
                onTick("      1").
                onTick("      2").go(4).
                onTick("      3").go(4).
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_go_to_first_direction_and_then_to_other() {
        assertThat(elevator).with(clock).call(0, UP).tick().go(4).
                onTick("CLOSE 0").
                onTick("      1").
                onTick("      2").
                onTick("      3").call(1, UP).
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("      3").
                onTick("      2").
                onTick("      1").
                onTick("OPEN   ").go(0).
                onTick("CLOSE  ").
                onTick("      0").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

}
