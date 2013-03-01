package elevator.server;

import java.net.URL;

public class Elevator {
    final Email email;

    public Elevator(Email email, URL url) {
        this.email = email;
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
