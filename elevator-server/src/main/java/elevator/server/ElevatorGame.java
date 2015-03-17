package elevator.server;

import elevator.*;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.InitializationStrategy;
import elevator.user.MaxNumberOfUsers;
import elevator.user.RandomUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Set;

import static elevator.server.ElevatorGame.State.INIT;
import static elevator.server.ElevatorGame.State.PAUSE;
import static elevator.server.ElevatorGame.State.RESUME;

class ElevatorGame {
    private static final String HTTP = "http";

    final Player player;
    final URL url;
    private final Building building;
    private final Score score;
    private final InitializationStrategy userInitializationStrategy;

    String lastErrorMessage;
    State state;

    ElevatorGame(Player player, URL url, MaxNumberOfUsers maxNumberOfUsers, Score initialScore) throws MalformedURLException {
        this(player, url, maxNumberOfUsers, initialScore, new RandomUser(), null);
    }

    ElevatorGame(Player player, URL url, MaxNumberOfUsers maxNumberOfUsers, Score initialScore, InitializationStrategy userInitializationStrategy, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.player = player;
        this.url = url;
        this.building = new Building(new HTTPElevator(url, urlStreamHandler), maxNumberOfUsers);
        this.score = initialScore;
        this.userInitializationStrategy = userInitializationStrategy;
        this.lastErrorMessage = null;
        this.state = INIT;
    }

    ElevatorGame stop() {
        state = PAUSE;
        return this;
    }

    ElevatorGame resume() {
        state = RESUME;
        return this;
    }

    int floor() {
        return building.floor();
    }

    public long travelingUsers() {
        return building.travelingUsers();
    }

    Boolean doorIsOpen() {
        return Door.OPEN.equals(building.door());
    }

    public void updateState() {
        if (state == PAUSE) {
            return;
        }
        if (init()) {
            reset("the elevator is at floor 0 and its doors are closed");
            state = RESUME;
            return;
        }
        try {
            building.addUser(userInitializationStrategy);
            building.updateBuildingState().forEach(score::success);
        } catch (ElevatorIsBrokenException e) {
            reset(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorGame elevatorGame = (ElevatorGame) o;

        return player.equals(elevatorGame.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    PlayerInfo getPlayerInfo() {
        return new PlayerInfo(this, player);
    }

    Integer score() {
        return score.score;
    }

    Integer averageScore() {
        return score.getAverageScore();
    }

    void reset(String message) {
        if (!init()) {
            score.loose();
        }
        building.reset(message);
        lastErrorMessage = message;
    }

    public Set<FloorState> floorStates() {
        return building.floorStates();
    }

    enum State {
        INIT, RESUME, PAUSE;
    }

    private boolean init() {
        return state == INIT;
    }
}
