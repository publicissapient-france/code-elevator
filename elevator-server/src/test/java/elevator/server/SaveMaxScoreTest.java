package elevator.server;

import elevator.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static elevator.server.SaveMaxScore.SCORES_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SaveMaxScoreTest {
    @Before
    public void clearScores() throws IOException {
        try (ObjectOutputStream in = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            in.writeObject(new HashMap<String, Integer>());
        }
    }

    @After
    public void deleteScores() {
        boolean isDeleted = SCORES_FILE.delete();
        assertThat(isDeleted).as("scores files is deleted").isTrue();
    }

    @Test
    public void should_save_score_when_success() throws IOException, ClassNotFoundException {
        Score score = new Score();
        new SaveMaxScore(score, new Player("email@provider.net"));

        score.success(user(0, 5, 7, 0));

        assertThat(scoresFromFile()).as("scores from file").containsOnly(entry("email@provider.net", 20));
    }

    private Map<String, Integer> scoresFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            return (Map<String, Integer>) in.readObject();
        }
    }

    private User user(int floor, int floorToGo, int tickToGo, int tickToWait) {
        User user = mock(User.class);
        doReturn(floor).when(user).getInitialFloor();
        doReturn(floorToGo).when(user).getFloorToGo();
        doReturn(tickToGo).when(user).getTickToGo();
        doReturn(tickToWait).when(user).getTickToWait();
        return user;
    }
}
