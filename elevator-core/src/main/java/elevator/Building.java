package elevator;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class Building {

    private static final Integer MAX_NUMBER_OF_USERS = 10;
    private final HashSet<User> users;

    public Building(Elevator elevator) {
        this.users = new HashSet<>();
    }

    public Building addUser() {
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
