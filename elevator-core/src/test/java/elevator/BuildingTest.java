package elevator;

import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.Set;

import static elevator.Command.*;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static elevator.engine.assertions.Assertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingTest {

    @Mock
    public ElevatorEngine elevator;

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void should_add_user() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());

        building.addUser();

        assertThat(building).users().hasSize(1);
    }

    @Test
    public void should_not_add_more_than_max_number_of_users() {
        final ConstantMaxNumberOfUsers maxNumberOfUsers = new ConstantMaxNumberOfUsers();
        final Integer MAX_NUMBER_OF_USERS = maxNumberOfUsers.value();
        final Building building = new Building(elevator, maxNumberOfUsers);
        for (Integer i = 1; i <= MAX_NUMBER_OF_USERS; i++) {
            building.addUser();
        }
        assertThat(building).users().hasSize(MAX_NUMBER_OF_USERS);

        building.addUser();

        assertThat(building).users().hasSize(MAX_NUMBER_OF_USERS);
    }

    @Test
    public void should_set_max_number_of_users_to_zero_if_max_number_of_users_is_negative() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers(-4));

        building.addUser();

        assertThat(building).users().hasSize(0);
    }

    @Test
    public void should_have_an_initial_state() throws Exception {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());

        assertThat(building).doorIs(Door.CLOSE).floorIs(LOWER_FLOOR);
    }

    @Test
    public void should_restore_initial_state_if_reseted() throws Exception {
        when(elevator.nextCommand()).thenReturn(UP);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.updateBuildingState();
        building.addUser();

        building.reset();

        assertThat(building).doorIs(Door.CLOSE).floorIs(LOWER_FLOOR).users().isEmpty();
    }

    @Test
    public void should_does_nothing_if_elevator_does_nothing() {
        when(elevator.nextCommand()).thenReturn(NOTHING);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());

        building.updateBuildingState();

        assertThat(building).doorIs(Door.CLOSE).floorIs(LOWER_FLOOR);
    }

    @Test
    public void should_throws_exception_if_elevator_closes_doors_but_doors_are_already_closed() {
        when(elevator.nextCommand()).thenReturn(CLOSE);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't close doors");
        building.updateBuildingState();
    }

    @Test
    public void should_throw_exception_if_elevator_opens_doors_but_doors_are_already_open() {
        when(elevator.nextCommand()).thenReturn(OPEN, OPEN);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.updateBuildingState();

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't open doors because they aren't closed");
        building.updateBuildingState();
    }

    @Test
    public void should_throws_exception_if_elevator_goes_down_but_is_already_at_lower_floor() {
        when(elevator.nextCommand()).thenReturn(DOWN);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't go down because current floor is the lowest floor");
        building.updateBuildingState();
    }

    @Test
    public void should_throws_exception_if_elevator_goes_down_but_doors_are_open() {
        when(elevator.nextCommand()).thenReturn(UP, Command.OPEN, DOWN);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.updateBuildingState();
        building.updateBuildingState();

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't go down because doors are opened");
        building.updateBuildingState();
    }

    @Test
    public void should_throws_exception_if_elevator_goes_up_but_is_already_at_higher_floor() {
        when(elevator.nextCommand()).thenReturn(UP, UP, UP, UP, UP, UP);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.updateBuildingState();
        building.updateBuildingState();
        building.updateBuildingState();
        building.updateBuildingState();
        building.updateBuildingState();

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't go up because current floor is the highest floor");
        building.updateBuildingState();
    }

    @Test
    public void should_throws_exception_if_elevator_goes_up_but_doors_are_open() {
        when(elevator.nextCommand()).thenReturn(Command.OPEN, UP);
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.updateBuildingState();

        expectedException.expect(ElevatorIsBrokenException.class);
        expectedException.expectMessage("can't go up because doors are opened");
        building.updateBuildingState();
    }

    @Test
    public void shoud_count_traveling_users() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers());
        building.addUser();
        building.addUser();
        User user = building.users().iterator().next();
        travel(user);

        int travelingUsers = building.travelingUsers();

        assertThat(travelingUsers).isEqualTo(1);
    }

    @Test
    public void should_know_how_many_users_are_waiting() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers()).
                addUser().
                addUser().
                addUser();
        Iterator<User> users = building.users().iterator();
        users.next().setCurrentFloor(5);
        users.next().setCurrentFloor(5);
        users.next().setCurrentFloor(0);

        int[] waitingUsersByFloor = building.waitingUsersByFloors();

        assertThat(waitingUsersByFloor).isEqualTo(new int[]{
                1, 0, 0, 0, 0, 2
        });
    }

    @Test
    public void should_get_all_waiting_users() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers()).
                addUser().
                addUser().
                addUser();
        Iterator<User> users = building.users().iterator();
        User first = users.next();
        travel(first);
        User second = users.next();
        User third = users.next();

        Set<User> waitingUsers = building.waitingUsers();

        assertThat(waitingUsers).containsOnly(second, third);
    }

    @Test
    public void should_get_floor_button_states_in_elevator() {
        Building building = new Building(elevator, new ConstantMaxNumberOfUsers()).
                addUser().
                addUser();
        Iterator<User> users = building.users().iterator();
        User user = users.next();
        Integer floorWhereUserWantsToGo = user.getFloorToGo();
        travel(user);

        boolean[] floorButtonStatesInElevator = building.getFloorButtonStatesInElevator();

        assertThat(floorButtonStatesInElevator).isEqualTo(new boolean[]{
                floorWhereUserWantsToGo == 0,
                floorWhereUserWantsToGo == 1,
                floorWhereUserWantsToGo == 2,
                floorWhereUserWantsToGo == 3,
                floorWhereUserWantsToGo == 4,
                floorWhereUserWantsToGo == 5
        });
    }

    private void travel(User user) {
        user.setCurrentFloor(user.getFloorToGo());
        user.elevatorIsOpen(user.getFloorToGo());
    }

}
