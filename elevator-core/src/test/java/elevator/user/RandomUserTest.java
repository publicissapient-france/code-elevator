package elevator.user;

import org.junit.Test;

import java.util.Random;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.user.Assertions.assertThat;

public class RandomUserTest {

    @Test
    public void should_create_random_user_at_floor_zero() {
        final RandomUser randomUser = new RandomUser(new Random(4096));

        assertThat(randomUser.create()).initialFloor(0).initialDirection(UP).floorToGo(3);
    }

    @Test
    public void should_create_random_user_at_floor_zero_even_if_first_call_to_next_int_returns_zero() {
        RandomUser randomUser = new RandomUser(new Random(4161));

        assertThat(randomUser.create()).floorToGo(3);
    }

    @Test
    public void should_create_random_user_at_other_floor() {
        final RandomUser randomUser = new RandomUser(new Random(0));

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(0);
    }

    @Test
    public void should_create_random_user_at_other_floor_going_to_zero() {
        final RandomUser randomUser = new RandomUser(new Random(0));

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(0);
    }

    @Test
    public void should_create_random_user_at_other_floor_even_if_first_call_to_next_int_returns_zero() {
        final RandomUser randomUser = new RandomUser(new Random(2792));

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(2);
    }

}
