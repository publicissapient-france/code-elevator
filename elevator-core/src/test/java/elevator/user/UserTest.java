package elevator.user;

import elevator.Elevator;
import elevator.engine.ElevatorEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private ElevatorEngine mockElevatorEngine;

    @Mock
    private Elevator mockElevator;

    @Test
    public void should_not_count_tick_to_go_when_not_traveling() {
        User user = new User(mockElevatorEngine, new FloorsAndDirection(0, 1));

        user.tick();

        assertThat(user.getTickToGo()).isEqualTo(0);
    }

    @Test
    public void should_count_tick_to_go_when_traveling() {
        User user = new User(mockElevatorEngine, new FloorsAndDirection(LOWER_FLOOR, HIGHER_FLOOR));
        // Make the user entering the elevator => traveling
        user.elevatorIsOpen(mockElevator, LOWER_FLOOR);

        user.tick();

        assertThat(user.getTickToGo()).isEqualTo(1);
    }

    @Test
    public void should_not_count_tick_to_wait_when_not_waiting() {
        User user = new User(mockElevatorEngine, new FloorsAndDirection(LOWER_FLOOR, HIGHER_FLOOR));
        // Make the user entering the elevator => traveling
        user.elevatorIsOpen(mockElevator, LOWER_FLOOR);

        user.tick();

        assertThat(user.getTickToWait()).isEqualTo(0);
    }

    @Test
    public void should_count_tick_to_wait_when_waiting() {
        User user = new User(mockElevatorEngine, new FloorsAndDirection(LOWER_FLOOR, HIGHER_FLOOR));

        user.tick();

        assertThat(user.getTickToWait()).isEqualTo(1);
    }

    @Test
    public void should_not_set_current_floor_if_user_is_not_traveling() {
        final User user = new User(mockElevatorEngine, new FloorsAndDirection(LOWER_FLOOR, HIGHER_FLOOR));

        user.elevatorIsAt(3);

        assertThat(user.at(3)).isFalse();
    }

}
