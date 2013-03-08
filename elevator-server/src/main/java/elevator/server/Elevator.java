package elevator.server;

import java.net.URL;
import elevator.Command;
import elevator.Direction;

public class Elevator implements elevator.Elevator {

    final Email email;

    public Elevator(Email email, URL url) {
        this.email = email;
    }

    @Override
    public elevator.Elevator call(Integer atFloor, Direction to) {
        throw new RuntimeException();
    }

    @Override
    public elevator.Elevator go(Integer floorToGo) {
        throw new RuntimeException();
    }

    @Override
    public Command nextCommand() {
        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Elevator elevator = (Elevator) o;

        return email.equals(elevator.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}
