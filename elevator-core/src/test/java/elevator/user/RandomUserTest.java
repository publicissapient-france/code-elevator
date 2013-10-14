package elevator.user;

import org.junit.Test;

import java.util.Random;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomUserTest {

    @Test
    public void should_have_string_representation() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(1);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.toString()).isEqualTo("user from floor 0 to 1 UP");
    }

    @Test
    public void should_create_random_user_at_floor_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(3);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.initialFloor()).isEqualTo(0);
        assertThat(randomUser.initialDirection()).isEqualTo(UP);
        assertThat(randomUser.floorToGo()).isEqualTo(3);
    }

    @Test
    public void should_create_random_user_at_floor_zero_even_if_first_call_to_next_int_returns_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(0).thenReturn(3);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.floorToGo()).isEqualTo(3);
    }

    @Test
    public void should_create_random_user_at_other_floor() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(TRUE).thenReturn(TRUE);
        when(mock.nextInt(6)).thenReturn(4).thenReturn(2);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.initialFloor()).isEqualTo(4);
        assertThat(randomUser.initialDirection()).isEqualTo(DOWN);
        assertThat(randomUser.floorToGo()).isEqualTo(2);
    }

    @Test
    public void should_create_random_user_at_other_floor_going_to_zero() {
        final Random mock = mock(Random.class);
        when(mock.nextBoolean()).thenReturn(TRUE).thenReturn(FALSE);
        when(mock.nextInt(6)).thenReturn(4);

        final RandomUser randomUser = new RandomUser(mock);

        assertThat(randomUser.initialFloor()).isEqualTo(4);
        assertThat(randomUser.initialDirection()).isEqualTo(DOWN);
        assertThat(randomUser.floorToGo()).isEqualTo(0);
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

        assertThat(randomUser.initialFloor()).isEqualTo(4);
        assertThat(randomUser.initialDirection()).isEqualTo(DOWN);
        assertThat(randomUser.floorToGo()).isEqualTo(2);
    }

}
