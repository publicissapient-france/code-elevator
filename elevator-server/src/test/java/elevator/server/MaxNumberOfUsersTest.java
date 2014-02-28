package elevator.server;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MaxNumberOfUsersTest {
    @Test
    public void should_start_at_three() throws Exception {
        MaxNumberOfUsers maxNumberOfUsers = new MaxNumberOfUsers();

        assertThat(maxNumberOfUsers.value()).isEqualTo(3);
    }

    @Test
    public void should_increase() throws Exception {
        MaxNumberOfUsers maxNumberOfUsers = new MaxNumberOfUsers();

        Integer increasedValue = maxNumberOfUsers.increase();

        assertThat(increasedValue).as("default max number of users increased by one")
                .isEqualTo(4)
                .isEqualTo(maxNumberOfUsers.value());
    }

    @Test
    public void should_decrease() throws Exception {
        MaxNumberOfUsers maxNumberOfUsers = new MaxNumberOfUsers();

        Integer decreasedValue = maxNumberOfUsers.decrease();

        assertThat(decreasedValue).as("max number of users of 3 decreased by one")
                .isEqualTo(2);
    }

    @Test
    public void should_not_decrease_less_than_zero() throws Exception {
        MaxNumberOfUsers maxNumberOfUsers = new MaxNumberOfUsers();
        maxNumberOfUsers.decrease();
        maxNumberOfUsers.decrease();
        maxNumberOfUsers.decrease();

        Integer decreasedValue = maxNumberOfUsers.decrease();

        assertThat(decreasedValue).as("default max number of users decreased by four")
                .isEqualTo(0);
    }
}
