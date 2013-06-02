package elevator.server;

import elevator.Building;
import elevator.Clock;
import elevator.ClockListener;

import java.net.MalformedURLException;
import java.net.URL;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    final Player player;

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
    }

    ElevatorGame start() {
        clock.addClockListener(this);
        elevatorEngine.reset();
        return this;
    }

    ElevatorGame stop() {
        clock.removeClockListener(this);
        return this;
    }

    int floor() {
        return building.floor();
    }

    Score score() {
        return elevatorEngine.getScore();
    }

    public int travelingUsers() {
        return building.travelingUsers();
    }

    public int[] waitingUsersByFloors() {
        return building.waitingUsersByFloors();
    }

    @Override
    public ClockListener onTick() {
        if (elevatorEngine.hasTransportError()) {
            stop();
            return this;
        }
        building.addUser();
        building.updateBuildingState();
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

}
