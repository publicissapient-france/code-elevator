package elevator;

import elevator.engine.ElevatorEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static elevator.Building.MAX_NUMBER_OF_USERS;
import static elevator.Command.*;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static elevator.engine.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuildingTest {

    @Mock
    public ElevatorEngine elevator;

    @Test
    public void should_add_user() {
        Building building = new Building(elevator);

        building.addUser();

        assertThat(building).users().hasSize(1);
    }

    @Test
    public void should_not_add_more_than_max_number_of_users() {
        Building building = new Building(elevator);
        for (Integer i = 1; i <= MAX_NUMBER_OF_USERS; i++) {
            building.addUser();
        }
        assertThat(building).users().hasSize(MAX_NUMBER_OF_USERS);

        building.addUser();

        assertThat(building).users().hasSize(MAX_NUMBER_OF_USERS);
    }

    @Test
    public void should_have_an_initial_state() throws Exception {
        Building building = new Building(elevator);

        assertThat(building).doorIs(Door.CLOSE).floorIs(LOWER_FLOOR);
    }

    @Test
    public void should_restore_initial_state_if_reseted() throws Exception {
        when(elevator.nextCommand()).thenReturn(UP, UP, OPEN, OPEN);
        Building building = new Building(elevator);
        building.updateBuildingState().updateBuildingState().updateBuildingState();
        building.addUser();
        verify(elevator, never()).reset();
        assertThat(building).doorIs(Door.OPEN).floorIs(2).users().hasSize(1);

        building.updateBuildingState();

        verify(elevator).reset();
        assertThat(building).doorIs(Door.CLOSE).floorIs(LOWER_FLOOR).users().isEmpty();
    }

    @Test
    public void should_does_nothing_if_elevator_does_nothing() {
        when(elevator.nextCommand()).thenReturn(NOTHING);
        Building building = new Building(elevator);

        building.updateBuildingState();

        verify(elevator, never()).reset();
    }

    @Test
    public void should_reset_if_elevator_closes_doors_but_doors_are_already_closed() {
        when(elevator.nextCommand()).thenReturn(CLOSE);
        Building building = new Building(elevator);

        building.updateBuildingState();

        verify(elevator).reset();
    }

    @Test
    public void should_reset_if_elevator_opens_doors_but_doors_are_already_open() {
        when(elevator.nextCommand()).thenReturn(OPEN, OPEN);
        Building building = new Building(elevator);
        building.updateBuildingState();
        verify(elevator, never()).reset();

        building.updateBuildingState();

        verify(elevator).reset();
    }

    @Test
    public void should_reset_if_elevator_goes_down_but_is_already_at_lower_floor() {
        when(elevator.nextCommand()).thenReturn(DOWN);
        Building building = new Building(elevator);
        verify(elevator, never()).reset();

        building.updateBuildingState();

        verify(elevator).reset();
    }

    @Test
    public void should_reset_if_elevator_goes_down_but_doors_are_open() {
        when(elevator.nextCommand()).thenReturn(UP, Command.OPEN, DOWN);
        Building building = new Building(elevator);
        building.updateBuildingState().updateBuildingState();
        verify(elevator, never()).reset();

        building.updateBuildingState();

        verify(elevator).reset();
    }

    @Test
    public void should_reset_if_elevator_goes_up_but_is_already_at_higher_floor() {
        when(elevator.nextCommand()).thenReturn(UP, UP, UP, UP, UP, UP);
        Building building = new Building(elevator);
        building.updateBuildingState().updateBuildingState().updateBuildingState().updateBuildingState().updateBuildingState();
        verify(elevator, never()).reset();

        building.updateBuildingState();

        verify(elevator).reset();
    }

    @Test
    public void should_reset_if_elevator_goes_up_but_doors_are_open() {
        when(elevator.nextCommand()).thenReturn(Command.OPEN, UP);
        Building building = new Building(elevator);
        building.updateBuildingState();
        verify(elevator, never()).reset();

        building.updateBuildingState();

        verify(elevator).reset();
    }

}
