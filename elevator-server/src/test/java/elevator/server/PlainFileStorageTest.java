package elevator.server;

import elevator.user.User;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import static elevator.server.PlainFileStorage.SCORE_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class PlainFileStorageTest {
	private StorageService storageSvc = null;

	@Before
	public void initTest(){
		if(SCORE_FILE.exists()){
			assertThat(SCORE_FILE.delete()).as("Score file should be deleted before each PlainFileStorageTest.*").isTrue();
		}
		ServiceLoader<StorageService> storageLoader;
		if(storageSvc==null){
			storageLoader = ServiceLoader.load(StorageService.class);
		}else{
			storageLoader = ServiceLoader.load(StorageService.class);
			storageLoader.reload();
		}
		storageSvc = storageLoader.iterator().next(); //J'ai de la chance :D
		assertThat(storageSvc).as("StorageService should be a PlainFileStorage").isInstanceOf(PlainFileStorage.class);
		((PlainFileStorage)storageSvc).syncDelay = 5000;

		assertThat(SCORE_FILE.exists()).as("Score file should exists before each test").isTrue();
		assertThat(SCORE_FILE.length()).as("Score file size should be 0 before each test").isEqualTo(0);
	}

	@After
	public void cleanTest(){
		assertThat(SCORE_FILE.exists()).as("Score file should exists after each test").isTrue();
		assertThat(SCORE_FILE.delete()).as("Score file should be deleted after each test").isTrue();
	}

	@Test
	public void hasScored_and_scoreFile_modified() throws InterruptedException{
		final long scoreFileLastModified0 = SCORE_FILE.lastModified();
		final long scoreFileLength0 = SCORE_FILE.length();
		final Player p1 = new Player("bob@yopmail.com", "Bob");
		final Player p2 = new Player("jane@yopmail.com", "Jane");
		assertThat(storageSvc.hasScored(p1)).as("hasScored() when nobody has scored").isFalse();
		assertThat(storageSvc.getScore(p1)).as("p1 score when nobody has scored").isNull();
		assertThat(storageSvc.hasScored(p2)).as("hasScored() when nobody has scored").isFalse();
		assertThat(storageSvc.getScore(p2)).as("p1 score when nobody has scored").isNull();
		Thread.sleep(1000); //without this wait, next assert are not significant
		assertThat(SCORE_FILE.lastModified()).as("File last modified time").isEqualTo(scoreFileLastModified0);
		assertThat(SCORE_FILE.length()).as("File length").isEqualTo(scoreFileLength0);

		final Score score1 = new Score();
		new SaveMaxScore(score1, p1, storageSvc);
		assertThat(storageSvc.hasScored(p1)).as("hasScored() when SaveMaxScore just registered for p1").isTrue();
		assertThat(storageSvc.hasScored(p2)).as("hasScored() for p2").isFalse();
		assertThat(storageSvc.getScore(p1)).as("p1 score when when SaveMaxScore just registered for p1").isEqualTo(score1);

		Thread.sleep(1000);
		assertThat(SCORE_FILE.lastModified()).as("File last modified time").isEqualTo(scoreFileLastModified0);
		assertThat(SCORE_FILE.length()).as("File length").isEqualTo(scoreFileLength0);

		Thread.sleep(((PlainFileStorage) storageSvc).syncDelay);
		score1.success(user(0, 5, 7, 0));
		assertThat(storageSvc.getScore(p1)).as("p1 score when success").isEqualTo(score1);
		Thread.sleep(1000);
		assertThat(SCORE_FILE.lastModified()).as("File last modified time").isGreaterThan(scoreFileLastModified0);
		assertThat(SCORE_FILE.length()).as("File length").isGreaterThan(scoreFileLength0);
	}

	@Test
	public void getAllScores_from_file() throws IOException, URISyntaxException{
		assertThat(storageSvc.getAllScores()).as("All scores at game start").isEmpty();

		generate4scoresFile();
		((PlainFileStorage) storageSvc).forceScoreReload();
		Map<String, Score> expectedScores = new HashMap<>();
		expectedScores.put("sally.sfaction@yopmail.com", new Score(20, new DateTime(2014,3,14,20,54,43)));
		expectedScores.put("john.doe@yopmail.com", new Score(40, new DateTime(2014,3,14,20,20,20)));
		expectedScores.put("jane.doe@yopmail.com", new Score(80, new DateTime(2014,3,15,10,10,10)));
		expectedScores.put("marc.asmerged@yopmail.com", new Score(50, new DateTime(2014,3,15,11,11,11)));

		Map<String, Score> allScores = storageSvc.getAllScores();
		assertThat(allScores.size()).as("Score count in cache after 4 scores file reload").isEqualTo(expectedScores.size());
		String[] expectedKeys = expectedScores.keySet().toArray(new String[expectedScores.size()]);
		assertThat(allScores).containsKeys(expectedKeys);
		for(String expectedKey: expectedKeys){
			Score expectedScore = expectedScores.get(expectedKey);
			Score actualScore = allScores.get(expectedKey);
			assertThat(actualScore.score).isEqualTo(expectedScore.score);
			assertThat(actualScore.started).isEqualTo(expectedScore.started);
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

	private void generate4scoresFile() throws IOException, URISyntaxException{
		URL srcUrl = Thread.currentThread().getContextClassLoader().getResource("flat_scores_4scores");
		assertThat(srcUrl).as("URL of resource file with 4 scores").isNotNull();
		try(FileChannel source = new FileInputStream(new File(srcUrl.toURI())).getChannel(); FileChannel dest=new FileOutputStream(SCORE_FILE).getChannel()){
			source.transferTo(0,source.size(),dest);
		}
	}
}
