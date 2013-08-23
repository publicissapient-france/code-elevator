package elevator.server;

import elevator.User;
import org.junit.Test;

import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ScoreTest {

    @Test
    public void should_compute_scores_with_max_amplitude() {
        String scores = scores(0, 5);

        assertThat(scores).isEqualTo("" +
                "       \\tickToWait |\n" +
                "tickToGo\\          |  0  1  2  3  4  5  6  7  8  9\n" +
                "-------------------+------------------------------\n" +
                "                 0 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 1 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 2 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 3 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 4 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 5 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 6 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 7 |  X 20 19 19 18 18 17 17 16 16\n" +
                "                 8 |  X 19 18 18 17 17 16 16 15 15\n" +
                "                 9 |  X 18 17 17 16 16 15 15 14 14\n" +
                "                10 |  X 17 16 16 15 15 14 14 13 13\n" +
                "                11 |  X 16 15 15 14 14 13 13 12 12\n" +
                "                12 |  X 15 14 14 13 13 12 12 11 11\n" +
                "                13 |  X 14 13 13 12 12 11 11 10 10\n" +
                "                14 |  X 13 12 12 11 11 10 10  9  9\n" +
                "                15 |  X 12 11 11 10 10  9  9  8  8\n" +
                "                16 |  X 11 10 10  9  9  8  8  7  7\n" +
                "                17 |  X 10  9  9  8  8  7  7  6  6\n" +
                "                18 |  X  9  8  8  7  7  6  6  5  5\n" +
                "                19 |  X  8  7  7  6  6  5  5  4  4\n" +
                "                20 |  X  7  6  6  5  5  4  4  3  3\n"
        );
    }

    @Test
    public void should_compute_scores_with_min_amplitude() {
        String scores = scores(4, 3);

        assertThat(scores).isEqualTo("" +
                "       \\tickToWait |\n" +
                "tickToGo\\          |  0  1  2  3  4  5  6  7  8  9\n" +
                "-------------------+------------------------------\n" +
                "                 0 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 1 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 2 |  X  X  X  X  X  X  X  X  X  X\n" +
                "                 3 |  X 20 19 19 18 18 17 17 16 16\n" +
                "                 4 |  X 19 18 18 17 17 16 16 15 15\n" +
                "                 5 |  X 18 17 17 16 16 15 15 14 14\n" +
                "                 6 |  X 17 16 16 15 15 14 14 13 13\n" +
                "                 7 |  X 16 15 15 14 14 13 13 12 12\n" +
                "                 8 |  X 15 14 14 13 13 12 12 11 11\n" +
                "                 9 |  X 14 13 13 12 12 11 11 10 10\n" +
                "                10 |  X 13 12 12 11 11 10 10  9  9\n" +
                "                11 |  X 12 11 11 10 10  9  9  8  8\n" +
                "                12 |  X 11 10 10  9  9  8  8  7  7\n" +
                "                13 |  X 10  9  9  8  8  7  7  6  6\n" +
                "                14 |  X  9  8  8  7  7  6  6  5  5\n" +
                "                15 |  X  8  7  7  6  6  5  5  4  4\n" +
                "                16 |  X  7  6  6  5  5  4  4  3  3\n" +
                "                17 |  X  6  5  5  4  4  3  3  2  2\n" +
                "                18 |  X  5  4  4  3  3  2  2  1  1\n" +
                "                19 |  X  4  3  3  2  2  1  1  0  0\n" +
                "                20 |  X  3  2  2  1  1  0  0  0  0\n"
        );
    }

    @Test
    public void should_loose_10_points() {
        Score score = new Score();

        Score loose = score.loose();

        assertThat(loose.score).isEqualTo(-10);
    }

    private User user(int floor, int floorToGo, int tickToGo, int tickToWait) {
        User user = mock(User.class);
        doReturn(floor).when(user).getInitialFloor();
        doReturn(floorToGo).when(user).getFloorToGo();
        doReturn(tickToGo).when(user).getTickToGo();
        doReturn(tickToWait).when(user).getTickToWait();
        return user;
    }

    private String scores(Integer floor, Integer floorToGo) {
        StringBuilder out = new StringBuilder();
        out.append("       \\tickToWait |\n");
        out.append("tickToGo\\          |");
        for (int tickToWait = 0; tickToWait < 10; tickToWait++) {
            out.append(format("%3d", tickToWait));
        }
        out.append('\n');
        out.append("-------------------+------------------------------\n");
        for (int tickToGo = 0; tickToGo <= 20; tickToGo++) {
            out.append(format("             %5d |", tickToGo));
            for (int tickToWait = 0; tickToWait < 10; tickToWait++) {
                try {
                    out.append(format("%3d", new Score().success(user(floor, floorToGo, tickToGo, tickToWait)).score));
                } catch (IllegalStateException e) {
                    out.append(format("%3s", "X"));
                }
            }
            out.append('\n');
        }
        return out.toString();
    }

}
