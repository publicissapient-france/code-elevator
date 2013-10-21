package elevator.user;

import org.junit.Test;

import java.util.Random;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.user.Assertions.assertThat;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomUserTest {

    @Test
    public void should_create_random_user_at_floor_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(3);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.create()).initialFloor(0).initialDirection(UP).floorToGo(3);
    }

    @Test
    public void should_create_random_user_at_floor_zero_even_if_first_call_to_next_int_returns_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(0).thenReturn(3);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.create()).floorToGo(3);
    }

    @Test
    public void should_create_random_user_at_other_floor() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(TRUE).thenReturn(TRUE);
        when(mock.nextInt(6)).thenReturn(4).thenReturn(2);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(2);
    }

    @Test
    public void should_create_random_user_at_other_floor_going_to_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(TRUE).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(4);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(0);
    }

    @Test
    public void should_create_random_user_at_other_floor_even_if_first_call_to_next_int_returns_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(TRUE).thenReturn(TRUE);
        when(mock.nextInt(6)).thenReturn(4).
                thenReturn(4). // already used by initialFloor
                thenReturn(0). // forbidden
                thenReturn(2);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.create()).initialFloor(4).initialDirection(DOWN).floorToGo(2);
    }

}
