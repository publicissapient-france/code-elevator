package elevator.server;

import elevator.*;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.InitializationStrategy;
import elevator.user.MaxNumberOfUsers;
import elevator.user.RandomUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Set;

import static elevator.server.ElevatorGame.State.PAUSE;
import static elevator.server.ElevatorGame.State.RESUME;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    final Player player;
    final URL url;
    private final Clock clock;
    private final ElevatorEngine elevatorEngine;
    private final Building building;
    private final Score score;
    private final InitializationStrategy userInitializationStrategy;

    String lastErrorMessage;
    State state;

    ElevatorGame(Player player, URL url, MaxNumberOfUsers maxNumberOfUsers, Clock clock, Score initialScore) throws MalformedURLException {
        this(player, url, maxNumberOfUsers, clock, initialScore, new RandomUser(), null);
    }

    ElevatorGame(Player player, URL url, MaxNumberOfUsers maxNumberOfUsers, Clock clock, Score initialScore, InitializationStrategy userInitializationStrategy, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.player = player;
        this.url = url;
        this.elevatorEngine = new HTTPElevator(url, clock.EXECUTOR_SERVICE, urlStreamHandler);
        this.building = new Building(elevatorEngine, maxNumberOfUsers);
        this.clock = clock;
        this.score = initialScore;
        this.userInitializationStrategy = userInitializationStrategy;
        this.lastErrorMessage = null;
        this.state = RESUME;
        this.resume();
    }

    ElevatorGame stop(Boolean shutdown) {
        clock.removeClockListener(this);
        if (shutdown) {
            ((HTTPElevator) elevatorEngine).shutdown();
        }
        state = PAUSE;
        return this;
    }

    ElevatorGame resume() {
        clock.addClockListener(this);
        state = RESUME;
        return this;
    }

    int floor() {
        return building.floor();
    }

    public int travelingUsers() {
        return building.travelingUsers();
    }

    Boolean doorIsOpen() {
        return Door.OPEN.equals(building.door());
    }

    @Override
    public void onTick() {
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
        score.loose();
        building.reset(message);
        lastErrorMessage = message;
    }

    public Set<FloorState> floorStates() {
        return building.floorStates();
    }

    enum State {
        RESUME, PAUSE
    }
}
