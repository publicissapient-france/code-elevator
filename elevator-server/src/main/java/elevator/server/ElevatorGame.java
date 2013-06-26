package elevator.server;

import elevator.*;
import elevator.exception.ElevatorIsBrokenException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    final Player player;
    final Score score;

    private final Clock clock;
    private final HTTPElevator elevatorEngine;
    private final Building building;

    ElevatorGame(Player player, URL url, Clock clock) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.player = player;
        this.elevatorEngine = new HTTPElevator(url, clock.EXECUTOR_SERVICE);
        this.building = new Building(elevatorEngine);
        this.clock = clock;
        this.score = new Score();
    }

    ElevatorGame start() {
        clock.addClockListener(this);
        reset();
        return this;
    }

    ElevatorGame stop() {
        clock.removeClockListener(this);
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
            score.loose();
            reset();
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

    public PlayerInfo getPlayerInfo() {
        return new PlayerInfo(this, player);
    }

    private void reset() {
        try {
            elevatorEngine.reset();
        } catch (ElevatorIsBrokenException e) {
            score.loose();
        }
    }

}
