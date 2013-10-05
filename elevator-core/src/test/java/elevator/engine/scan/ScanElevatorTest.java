package elevator.engine.scan;

import org.junit.Before;
import org.junit.Test;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.assertions.Assertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;

public class ScanElevatorTest {

    private ScanElevator elevator;

    @Before
    public void createElevator() {
        elevator = new ScanElevator();
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
        assertThat(elevator).onTick("CLOSE 0");
    }

    @Test
    public void should_open_doors() {
        assertThat(elevator).call(0, UP).
                onTick("OPEN 0");
    }

    @Test
    public void should_close_doors_after_call() {
        assertThat(elevator).call(0, UP).
                onTick("OPEN  0").
                onTick("CLOSE  ");
    }

    @Test
    public void should_call_elevator_and_open_doors() {
        assertThat(elevator).call(4, UP).
                onTick("      1").
                onTick("      2").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_go_to_floor() {
        assertThat(elevator).call(0, UP).tick().go(4).
                onTick("CLOSE 0").
                onTick("      1").
                onTick("      2").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").
                onTick("CLOSE  ");
    }

    @Test
    public void should_stop_at_two_floors() {
        assertThat(elevator).call(0, UP).tick().go(4).go(2).
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
    public void should_go_at_same_floor_for_any_times() {
        assertThat(elevator).call(0, UP).tick().go(4).
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
        assertThat(elevator).call(0, UP).tick().go(4).
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

    @Test
    public void should_not_open_doors_when_just_closing() {
        assertThat(elevator).call(0, UP).
        onTick("OPEN  0").go(2).
        onTick("CLOSE  ").call(0, UP).
        onTick("      1").
        onTick("      2").
        onTick("OPEN   ").
        onTick("CLOSE  ").
        onTick("      1").
        onTick("      0").
        onTick("OPEN   ").
        onTick("CLOSE  ");
    }

    @Test
    public void should_assume_that_user_enters_into_the_elevator_if_doors_are_open() {
        assertThat(elevator).call(0, UP).
        onTick("OPEN  0").go(2).call(0, UP).go(3).
        onTick("CLOSE  ").
        onTick("      1").
        onTick("      2").
        onTick("OPEN   ").
        onTick("CLOSE  ").
        onTick("      3").
        onTick("OPEN   ").
        onTick("CLOSE  ");
    }

    @Test
    public void should_run_scenario_1() {
        assertThat(elevator).reset("the elevator is at the...").call(2, DOWN).
                onTick("CLOSE 1").call(0, UP).call(0, UP).
                onTick("      2").
                onTick("OPEN   ").go(0).
                onTick("CLOSE  ").
                onTick("      1").
                onTick("      0").
                onTick("OPEN   ").go(2).go(1).call(0, UP).
                onTick("CLOSE  ").
                onTick("      1");
    }

    @Test
    public void should_run_scenario_2() {
        assertThat(elevator).reset("the elevator is at the...").call(4, DOWN).call(1, DOWN).
                onTick("CLOSE 1").call(0, UP).
                onTick("      2").
                onTick("      3").
                onTick("      4").
                onTick("OPEN   ").go(0).
                onTick("CLOSE  ").
                onTick("      3").
                onTick("      2").
                onTick("      1").
                onTick("OPEN   ").go(0).
                onTick("CLOSE  ").
                onTick("      0").
                onTick("OPEN   ").go(3).call(4, DOWN).
                onTick("CLOSE  ").call(0, UP).
                onTick("      1");
    }

    @Test
    public void should_run_scenario_3() {
        assertThat(elevator).reset("the elevator is at the...").call(5, DOWN).
                onTick("CLOSE 1").call(3, DOWN).
                onTick("      2").call(0, UP).
                onTick("      3").
                onTick("      4").
                onTick("      5").
                onTick("OPEN   ").go(0).
                onTick("CLOSE  ").
                onTick("      4").
                onTick("      3").
                onTick("OPEN   ").
                onTick("CLOSE  ").go(0).
                onTick("      2").
                onTick("      1").
                onTick("      0").
                onTick("OPEN   ").
                onTick("CLOSE  ").
                onTick("CLOSE  ").
                onTick("CLOSE  ");
    }

}
