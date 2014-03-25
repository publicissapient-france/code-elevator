package elevator.server;

import elevator.Clock;
import elevator.server.security.UserPasswordValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.SECONDS;

class ElevatorServer implements UserPasswordValidator {
    private final Map<Player, ElevatorGame> elevatorGames = new TreeMap<>();
    private final Clock clock = new Clock();

    private MaxNumberOfUsers maxNumberOfUsers = new MaxNumberOfUsers();
	private final StorageService storageSvc;

    ElevatorServer() {
		Iterator<StorageService> storageServices = ServiceLoader.load(StorageService.class).iterator();
		if(storageServices.hasNext()){
			storageSvc = storageServices.next();
		}else{
			throw new ServiceNotFoundException("No service implementation found for "+StorageService.class.getName());
		}
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                clock.tick();
            }
        }, 0, 1, SECONDS);
    }

    public ElevatorServer addElevatorGame(Player player, URL server) throws MalformedURLException {
        addElevatorGame(player, server, new Score());
        return this;
    }

    public void addElevatorGame(Player player, URL server, Score score) throws MalformedURLException {
        if (elevatorGames.containsKey(player)) {
            throw new IllegalStateException("a game with player " + player + " has already been added");
        }
        ElevatorGame elevatorGame = new ElevatorGame(player, server, maxNumberOfUsers, clock, score, storageSvc);
        elevatorGames.put(player, elevatorGame);
    }

    @Override
    public boolean validate(String email, String password) {
        ElevatorGame elevatorGame = elevatorGame(email, FALSE);
        if (elevatorGame == null) {
            return false;
        }
        Player player = elevatorGame.player;
        return player.email.equals(email) && player.password.value().equals(password);
    }

    void removeElevatorGame(String email) {
        ElevatorGame elevatorGame = elevatorGame(email, FALSE);
        if (elevatorGame != null) {
            elevatorGame.stop(TRUE);
            elevatorGames.remove(elevatorGame.player);
        }
    }

    void pauseElevatorGame(String email) {
        elevatorGame(email).stop(FALSE);
    }

    void resumeElevatorGame(String email) {
        elevatorGame(email).resume();
    }

    PlayerInfo getPlayerInfo(String email) throws PlayerNotFoundException {
        return elevatorGame(email).getPlayerInfo();
    }

    void resetPlayer(String email) {
        elevatorGame(email).reset("player has requested a reset");
    }

    public Collection<ElevatorGame> getUnmodifiableElevatorGames() {
        return Collections.unmodifiableCollection(elevatorGames.values());
    }

	public List<ScoreInfo> getMaxScores(){
		List<ScoreInfo> scores = storageSvc.getAllScores();
		Collections.sort(scores, ScoreInfo.byScore());
		return scores;
	}

    Integer getMaxNumberOfUsers() {
        return maxNumberOfUsers.value();
    }

    Integer increaseMaxNumberOfUsers() {
        return maxNumberOfUsers.increase();
    }

    Integer decreaseMaxNumberOfUsers() {
        return maxNumberOfUsers.decrease();
    }

    private ElevatorGame elevatorGame(String email) {
        return elevatorGame(email, TRUE);
    }

    private ElevatorGame elevatorGame(String email, Boolean failOnError) {
        Player player = new Player(email);
        if (!elevatorGames.containsKey(player)) {
            if (failOnError) {
                throw new PlayerNotFoundException(player.email);
            }
            return null;
        }
        return elevatorGames.get(player);
    }
}
