package elevator;

import elevator.client.Elevator;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class BuildingTest {

    @Test
    public void should_add_user() throws Exception {
        Building building = new Building(new Elevator());

        building.addUser();

        assertThat(building.users()).hasSize(1);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_add_more_than_ten_users() throws Exception {
        Building building = new Building(new Elevator());
        for (Integer i = 1; i <= 10; i++) {
            building.addUser();
        }

        assertThat(building.users()).hasSize(10);
        building.addUser();
        fail();
    }

}
