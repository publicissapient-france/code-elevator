package elevator.server;

import java.util.Map;

/**
 * Created by jojo on 04/03/2014.
 */
public interface StorageService {
	void saveScoreIfBetter(Score score, Player p);
	boolean hasScored(Player p);
	Score getScore(Player p);
	Score getScore(String playerId);
	Map<String, Score> getAllScores();
}
