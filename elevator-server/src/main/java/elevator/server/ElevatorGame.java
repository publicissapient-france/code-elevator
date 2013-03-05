package elevator.server;

import elevator.User;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class ElevatorGame {

    private static final Integer MAX_NUMBER_OF_USERS = 10;

    private final Elevator elevator;
    private Set<User> users;

    public ElevatorGame(Elevator elevator) {
        this.elevator = elevator;
        this.users = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorGame elevatorGame = (ElevatorGame) o;

        return elevator.equals(elevatorGame.elevator);
    }

    @Override
    public int hashCode() {
        return elevator.hashCode();
    }

    public ElevatorGame addUser() {
        if (users.size() >= 10) {
            throw new IllegalStateException("can't add more than " + MAX_NUMBER_OF_USERS + " users");
        }
        users.add(new User());
        return this;
    }

    public Set<User> users() {
        return unmodifiableSet(users);
    }

}
