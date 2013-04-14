package elevator.server;

import elevator.Building;

import java.net.MalformedURLException;
import java.net.URL;

public class ElevatorGame {

    private static final String HTTP = "http";

    private final Email email;
    private final Building building;

    public ElevatorGame(Email email, URL url) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.email = email;
        this.building = new Building(new HTTPElevator(url));
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

}
