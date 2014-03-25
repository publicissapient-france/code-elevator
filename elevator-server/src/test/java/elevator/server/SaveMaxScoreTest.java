package elevator.server;

import elevator.user.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;

import static elevator.server.PlainFileStorage.SCORE_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SaveMaxScoreTest {
	private static StorageService storageSvc;
	@BeforeClass
	public static void serviceSetup(){
		Iterator<StorageService> storageServices = ServiceLoader.load(StorageService.class).iterator();
		if(storageServices.hasNext()){
			storageSvc = storageServices.next();
		}else{
			throw new ServiceNotFoundException("No service implementation found for "+StorageService.class.getName());
		}
	}

	@AfterClass
	public static void cleanUp(){
		assertThat(SCORE_FILE.exists()).as("Score file should exists after each test").isTrue();
		assertThat(SCORE_FILE.delete()).as("Score file should be deleted after each test").isTrue();
	}

    @Test
    public void should_save_score_when_success() throws IOException, ClassNotFoundException {
        Score score = new Score();
		Player player = new Player("email@provider.net");
        new SaveMaxScore(score, player, storageSvc);

        score.success(user(0, 5, 7, 0));

		assertThat(storageSvc.getAllScores()).containsOnly(new ScoreInfo("", "email@provider.net", score.score, score.started));
		assertThat(storageSvc.getScore(player).score).isEqualTo(20);

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
