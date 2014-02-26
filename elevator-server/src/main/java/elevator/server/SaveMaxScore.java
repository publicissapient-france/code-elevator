package elevator.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

class SaveMaxScore implements Observer {
    static final File SCORES_FILE = new File("scores");

    private final Player player;
    private final Score score;

    SaveMaxScore(Score score, Player player) {
        this.score = score;
        this.player = player;
        this.score.addObserver(this);
        this.saveIfNotPresentOrGreater();
    }

    @Override
    public void update(Observable o, Object arg) {
        saveIfNotPresentOrGreater();
    }

    private synchronized void saveIfNotPresentOrGreater() {
        try {
            Map<String, Integer> playerAndScores = readScores();
            if (!playerAndScores.containsKey(player.email) || playerAndScores.get(player.email) < score.score) {
                writeScores(playerAndScores);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> readScores() throws IOException, ClassNotFoundException {
        if (!SCORES_FILE.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            return (Map<String, Integer>) in.readObject();
        }
    }

    private void writeScores(Map<String, Integer> playerAndScores) throws IOException {
        playerAndScores.put(player.email, score.score);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            out.writeObject(playerAndScores);
        }
    }
}
