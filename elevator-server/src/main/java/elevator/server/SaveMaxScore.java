package elevator.server;

import java.util.Observable;
import java.util.Observer;

class SaveMaxScore implements Observer {

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
}
