package elevator.server;

import java.util.List;

public interface StorageService {
	void saveScoreIfBetter(Score score, Player p);
	boolean hasScored(Player p);
	ScoreInfo getScore(Player p);
	ScoreInfo getScore(String playerId);
	List<ScoreInfo> getAllScores();
}
