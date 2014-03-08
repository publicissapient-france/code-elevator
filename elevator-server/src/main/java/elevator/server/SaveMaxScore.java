package elevator.server;

import java.io.*;
import java.util.*;

class SaveMaxScore implements Observer {
//    static final File SCORES_FILE = new File("scores");

    private final Player player;

	private final StorageService storageSvc;

    SaveMaxScore(Score score, Player player, StorageService storageSvc) {
        this.player = player;
        score.addObserver(this);
		storageSvc.saveScoreIfBetter(score, this.player);
		this.storageSvc = storageSvc;
    }

    @Override
    public void update(Observable o, Object arg) {
        //saveIfNotPresentOrGreater();
		if(o instanceof Score){
			storageSvc.saveScoreIfBetter((Score) o, player);
		}
    }

    /*private synchronized void saveIfNotPresentOrGreater() {
        try {
            Map<String, Integer> playerAndScores = readScores();
            if (!playerAndScores.containsKey(player.email) || playerAndScores.get(player.email) < score.score) {
                writeScores(playerAndScores);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    /*private static Map<String, Integer> readScores() throws IOException, ClassNotFoundException {
        if (!SCORES_FILE.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            return (Map<String, Integer>) in.readObject();
        }
    }*/

    /*private void writeScores(Map<String, Integer> playerAndScores) throws IOException {
        playerAndScores.put(player.email, score.score);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            out.writeObject(playerAndScores);
        }
    }*/

    /*public static void main(String[] args) throws IOException, ClassNotFoundException {
        for (Map.Entry<Integer, Set<String>> scoreAndPlayers : orderByScore(SaveMaxScore.readScores()).entrySet()) {
            for (String email : scoreAndPlayers.getValue()) {
                System.out.format("%45s %d%n", email, scoreAndPlayers.getKey());
            }
        }
    }*/

    /*private static Map<Integer, Set<String>> orderByScore(Map<String, Integer> scoresByPlayer) {
        Map<Integer, Set<String>> playersByScore = new TreeMap<>();
        for (Map.Entry<String, Integer> playerAndScore : scoresByPlayer.entrySet()) {
            if (!playersByScore.containsKey(playerAndScore.getValue())) {
                playersByScore.put(playerAndScore.getValue(), new HashSet<String>());
            }
            playersByScore.get(playerAndScore.getValue()).add(playerAndScore.getKey());
        }
        return playersByScore;
    }*/

	public StorageService getStorageSvc() {
		return storageSvc;
	}
}
