package elevator.server;

import elevator.Building;
import elevator.User;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class ElevatorGame {

    private final Email email;
    private final Building building;

    public ElevatorGame(Email email) {
        this.email = email;
        this.building = new Building(new Elevator());
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
