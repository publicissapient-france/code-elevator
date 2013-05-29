package elevator.server;

import elevator.Building;
import elevator.Clock;
import elevator.ClockListener;

import java.net.MalformedURLException;
import java.net.URL;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    public final PlayerInfo playerInfo;

    private final Clock clock;
    private final HTTPElevator elevatorEngine;
    private final Building building;

    ElevatorGame(Email email, URL url, Clock clock) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.playerInfo = new PlayerInfo(email);
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

    public Email email() {
        return playerInfo.email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorGame elevatorGame = (ElevatorGame) o;

        return playerInfo.equals(elevatorGame.playerInfo);
    }

    @Override
    public int hashCode() {
        return playerInfo.hashCode();
    }

    @Override
    public ClockListener onTick() {
        if (elevatorEngine.hasTransportError()) {
            stop();
            playerInfo.loose();
            return this;
        }
        building.addUser();
        building.updateBuildingState();
        playerInfo.onepoint();
        return this;
    }
}
