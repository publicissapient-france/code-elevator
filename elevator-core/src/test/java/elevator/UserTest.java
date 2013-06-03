package elevator;

import elevator.engine.ElevatorEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    ElevatorEngine mockElevatorEngine;

    @Test
    public void should_not_count_tick_to_go_when_not_traveling() {
        User user = new User(mockElevatorEngine);

        user.onTick();

        assertThat(user.getTickToGo()).isEqualTo(0);
    }

    @Test
    public void should_count_tick_to_go_when_traveling() {
        User user = new User(mockElevatorEngine);

        //Make the user entering the elevator=> traveling
        user.elevatorIsOpen(user.getFloor());

        user.onTick();

        assertThat(user.getTickToGo()).isEqualTo(1);
    }

    @Test
    public void should_not_count_tick_to_wait_when_not_waiting() {
        User user = new User(mockElevatorEngine);

        //Make the user entering the elevator=> traveling
        user.elevatorIsOpen(user.getFloor());

        user.onTick();

        assertThat(user.getTickToWait()).isEqualTo(0);
    }

    @Test
    public void should_count_tick_to_wait_when_waiting() {
        User user = new User(mockElevatorEngine);

        user.onTick();

        assertThat(user.getTickToWait()).isEqualTo(1);
    }

}
