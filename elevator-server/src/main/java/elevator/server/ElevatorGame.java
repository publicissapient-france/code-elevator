package elevator.server;

import elevator.*;
import elevator.exception.ElevatorIsBrokenException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static elevator.server.ElevatorGame.State.PAUSE;
import static elevator.server.ElevatorGame.State.RESUME;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    final Player player;
    final Score score;

    String lastErrorMessage;
    State state;

    private final Clock clock;
    private final HTTPElevator elevatorEngine;
    private final Building building;

    ElevatorGame(Player player, URL url, MaxNumberOfUsers maxNumberOfUsers, Clock clock) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.player = player;
        this.elevatorEngine = new HTTPElevator(url, clock.EXECUTOR_SERVICE);
        this.building = new Building(elevatorEngine, maxNumberOfUsers);
        this.clock = clock;
        this.score = new Score();
        this.lastErrorMessage = null;
        this.state = RESUME;
        this.resume();
        this.resetElevatorEngine("the elevator is at the lowest level and its doors are closed");
    }

    ElevatorGame stop() {
        clock.removeClockListener(this);
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

    public int[] waitingUsersByFloors() {
        return building.waitingUsersByFloors();
    }

    Boolean doorIsOpen() {
        return Door.OPEN.equals(building.door());
    }

    @Override
    public ClockListener onTick() {
        try {
            building.addUser();
            Set<User> doneUsers = building.updateBuildingState();
            for (User doneUser : doneUsers) {
                score.success(doneUser);
            }
        } catch (ElevatorIsBrokenException e) {
            reset(e.getMessage());
        }
        return this;
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

    void reset(String message) {
        building.reset();
        score.loose();
        lastErrorMessage = message;
        resetElevatorEngine(lastErrorMessage);
    }

    private void resetElevatorEngine(String cause) {
        try {
            elevatorEngine.reset(cause);
        } catch (ElevatorIsBrokenException e) {
            score.loose();
        }
    }

    enum State {
        RESUME, PAUSE
    }
}
