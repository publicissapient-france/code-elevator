package elevator;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class Building implements ClockListener {

    private static final Integer MAX_NUMBER_OF_USERS = 10;

    private final HashSet<User> users;
    private final Elevator elevator;

    private Door door;
    private Integer floor;

    public Building(Elevator elevator) {
        this.users = new HashSet<>();
        this.elevator = elevator;
        this.floor = 0;
        this.door = Door.CLOSE;
    }

    public Building addUser() {
        if (users.size() >= 10) {
            throw new IllegalStateException("can't add more than " + MAX_NUMBER_OF_USERS + " users");
        }

        User newUser = new User(elevator);
        users.add(newUser);
        return this;
    }

    public Set<User> users() {
        return unmodifiableSet(users);
    }

    public Integer floor() {
        return floor;
    }

    public Door door() {
        return door;
    }

    @Override
    public ClockListener onTick() {
        Command command = elevator.nextCommand();

        switch (command) {
            case CLOSE:
                door = Door.CLOSE;
                break;
            case OPEN:
                door = Door.OPEN;
                for (User user : users) {
                    user.elevatorIsOpen(floor);
                }
                break;
            case UP:
                door = Door.CLOSE;
                floor++;
                break;
            case DOWN:
                door = Door.CLOSE;
                floor--;
                break;
        }
        return this;
    }

}
