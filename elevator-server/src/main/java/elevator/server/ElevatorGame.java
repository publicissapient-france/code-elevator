package elevator.server;

import elevator.Building;
import elevator.Clock;
import elevator.ClockListener;

import java.net.MalformedURLException;
import java.net.URL;

public class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    public final Email email;

    private final Building building;
    private final Clock clock;

    public ElevatorGame(Email email, URL url, Clock clock) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.email = email;
        this.building = new Building(new HTTPElevator(url, clock.EXECUTOR_SERVICE));
        this.clock = clock;
    }

    ElevatorGame start() {
        clock.addClockListener(this);
        return this;
    }

    ElevatorGame stop() {
        clock.removeClockListener(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorGame elevatorGame = (ElevatorGame) o;

        return email.equals(elevatorGame.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public ClockListener onTick() {
        building.addUser();
        building.updateBuildingState();
        return this;
    }
}
