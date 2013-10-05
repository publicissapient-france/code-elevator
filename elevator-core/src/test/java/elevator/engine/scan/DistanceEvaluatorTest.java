package elevator.engine.scan;

import org.junit.Test;

import static elevator.engine.scan.ElevatorDirection.*;
import static elevator.engine.scan.assertions.Assertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;

public class DistanceEvaluatorTest {

    private static final int LOWER_FLOOR = 0;
    private static final int HIGHER_FLOOR = 5;

    @Test
    public void should_compute_distance_when_elevator_is_going_up() {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(3, UP), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator).distancesAre("" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "| floor | direction | distance | direction | distance |\n" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "|     0 |        UP |        7 |      NONE |        3 |\n" +
                "|     1 |        UP |        8 |      NONE |        2 |\n" +
                "|     2 |        UP |        9 |      NONE |        1 |\n" +
                "|     3 |        UP |        0 |      NONE |        0 |\n" +
                "|     4 |        UP |        1 |      NONE |        1 |\n" +
                "|     5 |      DOWN |        2 |      NONE |        2 |\n" +
                "|     4 |      DOWN |        3 |      NONE |        1 |\n" +
                "|     3 |      DOWN |        4 |      NONE |        0 |\n" +
                "|     2 |      DOWN |        5 |      NONE |        1 |\n" +
                "|     1 |      DOWN |        6 |      NONE |        2 |\n" +
                "+-------+-----------+----------+-----------+----------+"
        );
    }

    @Test
    public void should_compute_distance_when_elevator_is_going_down() {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(3, DOWN), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator).distancesAre("" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "| floor | direction | distance | direction | distance |\n" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "|     0 |        UP |        3 |      NONE |        3 |\n" +
                "|     1 |        UP |        4 |      NONE |        2 |\n" +
                "|     2 |        UP |        5 |      NONE |        1 |\n" +
                "|     3 |        UP |        6 |      NONE |        0 |\n" +
                "|     4 |        UP |        7 |      NONE |        1 |\n" +
                "|     5 |      DOWN |        8 |      NONE |        2 |\n" +
                "|     4 |      DOWN |        9 |      NONE |        1 |\n" +
                "|     3 |      DOWN |        0 |      NONE |        0 |\n" +
                "|     2 |      DOWN |        1 |      NONE |        1 |\n" +
                "|     1 |      DOWN |        2 |      NONE |        2 |\n" +
                "+-------+-----------+----------+-----------+----------+"
        );
    }

    @Test
    public void should_compute_distance_when_elevator_is_not_traveling() {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(3, NONE), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator).distancesAre("" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "| floor | direction | distance | direction | distance |\n" +
                "+-------+-----------+----------+-----------+----------+\n" +
                "|     0 |        UP |        3 |      NONE |        3 |\n" +
                "|     1 |        UP |        2 |      NONE |        2 |\n" +
                "|     2 |        UP |        1 |      NONE |        1 |\n" +
                "|     3 |        UP |        0 |      NONE |        0 |\n" +
                "|     4 |        UP |        1 |      NONE |        1 |\n" +
                "|     5 |      DOWN |        2 |      NONE |        2 |\n" +
                "|     4 |      DOWN |        1 |      NONE |        1 |\n" +
                "|     3 |      DOWN |        0 |      NONE |        0 |\n" +
                "|     2 |      DOWN |        1 |      NONE |        1 |\n" +
                "|     1 |      DOWN |        2 |      NONE |        2 |\n" +
                "+-------+-----------+----------+-----------+----------+"
        );
    }

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

    @Test
    public void should_measure_distance_between_zero_without_direction_and_an_up_Command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(0, NONE), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(1, UP))).isEqualTo(1);
    }

    @Test
    public void should_measure_distance_between_zero_without_direction_and_a_down_Command() throws Exception {
        DistanceEvaluator comparator = new DistanceEvaluator(new Command(0, NONE), LOWER_FLOOR, HIGHER_FLOOR);

        assertThat(comparator.getDistance(new Command(4, DOWN))).isEqualTo(4);
    }

}
