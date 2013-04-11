package elevator.engine.scan;

import org.junit.Test;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static org.fest.assertions.Assertions.assertThat;

public class DistanceEvaluatorTest {

    private static final int LOWER_FLOOR = 0;
    private static final int HIGHER_FLOOR = 5;

    @Test
    public void should_have_distance_to_zero_between_same_command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(4, DOWN), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(4, DOWN))).isEqualTo(0);
    }

    @Test
    public void should_measure_distance_between_zero_and_an_up_Command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(LOWER_FLOOR, UP), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(1, UP))).isEqualTo(1);
    }

    @Test
    public void should_measure_distance_between_zero_and_a_down_Command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(LOWER_FLOOR, UP), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(4, DOWN))).isEqualTo(6);
    }

    @Test
    public void should_measure_distance_between_down_and_up_Command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(3, DOWN), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(4, UP))).isEqualTo(7);
    }

    @Test
    public void should_measure_distance_between_two_up_Commands_begining_with_greater() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(3, UP), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(2, UP))).isEqualTo(9);
    }

}
