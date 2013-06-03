package elevator.server;

import elevator.User;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

public class ScoreTest {

    @Test
    public void should_compute_best_effort_12_from_0_to_5() {
        User user = user(0, 5, 0, 0);

        assertThat(new Score().bestEffort(user))
                .as("(5-0) * 2 + 2")
                .isEqualTo(12);
    }

    @Test
    public void should_score_2_for_user_delivered_10_moves_and_no_waiting_points() {
        User user = user(0, 5, 10, 20);

        assertThat(new Score().success(user).score)
                .as("(5-0) * 2 + 2 - 10 + 0")
                .isEqualTo(2);
    }

    @Test
    public void should_score_0_for_user_delivered_14_moves_and_no_waiting_points() {
        User user = user(0, 5, 14, 20);

        assertThat(new Score().score(user))
                .as("(5-0) * 2 + 2 - 14 < 0 => 0 + 0")
                .isEqualTo(0);
    }

    @Test
    public void should_score_5_for_user_delivered_14_moves_and_5_waiting_points() {
        User user = user(0, 5, 14, 5);

        assertThat(new Score().score(user))
                .as("(5-0) * 2 + 2 - 14 < 0 => 0 + 5 ")
                .isEqualTo(5);
    }

    private User user(int floor, int floorToGo, int tickToGo, int tickToWait) {
        User user = Mockito.mock(User.class);
        doReturn(floor).when(user).getInitialFloor();
        doReturn(floorToGo).when(user).getFloorToGo();
        doReturn(tickToGo).when(user).getTickToGo();
        doReturn(tickToWait).when(user).getTickToWait();
        return user;
    }
}
