package elevator;

import elevator.engine.naive.NaiveElevator;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UserTest {

    @Test
    public void should_not_count_tick_when_not_traveling() {
        User user = new User(new NaiveElevator());

        user.onTick();

        assertThat(user.getTickToGo()).isEqualTo(0);
    }

    @Test
    public void should_count_tick_when_traveling() {
        User user = new User(new NaiveElevator());

        //Make the user entering the elevator=> traveling
        user.elevatorIsOpen(user.getFloor());

        user.onTick();

        assertThat(user.getTickToGo()).isEqualTo(1);
    }
}
