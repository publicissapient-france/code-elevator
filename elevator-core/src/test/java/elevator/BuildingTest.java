package elevator;

import org.junit.Test;

import static elevator.Building.MAX_NUMBER_OF_USERS;
import static elevator.Command.*;
import static elevator.engine.assertions.Assertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;

public class BuildingTest {

    @Test
    public void should_add_user() {
        Building building = new Building(new MockElevatorEngine());

        building.addUser();

        assertThat(building).users().hasSize(1);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_more_than_max_number_of_users() {
        Building building = new Building(new MockElevatorEngine());
        for (Integer i = 1; i <= MAX_NUMBER_OF_USERS; i++) {
            building.addUser();
        }
        assertThat(building).users().hasSize(MAX_NUMBER_OF_USERS);

        building.addUser();
    }

    @Test
    public void should_have_an_initial_state() throws Exception {
        Building building = new Building(new MockElevatorEngine());

        assertThat(building).doorIs(Door.CLOSE).floorIs(0);
    }

    @Test
    public void should_restore_initial_state_if_reseted() throws Exception {
        MockElevatorEngine elevator = new MockElevatorEngine(UP, UP, OPEN, OPEN);
        Building building = new Building(elevator);
        building.onTick().onTick().onTick();
        building.addUser();
        assertThat(elevator.resetCalled()).isFalse();
        assertThat(building).doorIs(Door.OPEN).floorIs(2).users().hasSize(1);

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
        assertThat(building).doorIs(Door.CLOSE).floorIs(0).users().isEmpty();
    }

    @Test
    public void should_does_nothing_if_elevator_does_nothing() {
        MockElevatorEngine elevator = new MockElevatorEngine(NOTHING);
        Building building = new Building(elevator);

        building.onTick();

        assertThat(elevator.resetCalled()).isFalse();
    }

    @Test
    public void should_reset_if_elevator_closes_doors_but_doors_are_already_closed() {
        MockElevatorEngine elevator = new MockElevatorEngine(CLOSE);
        Building building = new Building(elevator);

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

    @Test
    public void should_reset_if_elevator_opens_doors_but_doors_are_already_open() {
        MockElevatorEngine elevator = new MockElevatorEngine(OPEN, OPEN);
        Building building = new Building(elevator);
        building.onTick();
        assertThat(elevator.resetCalled()).isFalse();

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

    @Test
    public void should_reset_if_elevator_goes_down_but_is_already_at_lower_floor() {
        MockElevatorEngine elevator = new MockElevatorEngine(DOWN);
        Building building = new Building(elevator);
        assertThat(elevator.resetCalled()).isFalse();

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

    @Test
    public void should_reset_if_elevator_goes_down_but_doors_are_open() {
        MockElevatorEngine elevator = new MockElevatorEngine(UP, Command.OPEN, DOWN);
        Building building = new Building(elevator);
        building.onTick().onTick();
        assertThat(elevator.resetCalled()).isFalse();

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

    @Test
    public void should_reset_if_elevator_goes_up_but_is_already_at_higher_floor() {
        MockElevatorEngine elevator = new MockElevatorEngine(UP, UP, UP, UP, UP, UP);
        Building building = new Building(elevator);
        building.onTick().onTick().onTick().onTick().onTick();
        assertThat(elevator.resetCalled()).isFalse();

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

    @Test
    public void should_reset_if_elevator_goes_up_but_doors_are_open() {
        MockElevatorEngine elevator = new MockElevatorEngine(Command.OPEN, UP);
        Building building = new Building(elevator);
        building.onTick();
        assertThat(elevator.resetCalled()).isFalse();

        building.onTick();

        assertThat(elevator.resetCalled()).isTrue();
    }

}
