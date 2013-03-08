package elevator.server;

import elevator.User;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class ElevatorGame {

    private static final Integer MAX_NUMBER_OF_USERS = 10;

    private final Email email;
    private final elevator.Elevator elevator;
    private Set<User> users;

    public ElevatorGame(Email email) {
        this.email = email;
        this.elevator = new Elevator();
        this.users = new HashSet<>();
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
