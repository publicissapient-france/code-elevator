package elevator;

import elevator.engine.ElevatorEngine;

import java.util.HashSet;
import java.util.Set;

import static elevator.Door.CLOSE;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.unmodifiableSet;

public class Building implements ClockListener {

    static final Integer MAX_NUMBER_OF_USERS = 10;

    private final HashSet<User> users;
    private final ElevatorEngine elevatorEngine;

    private Door door;
    private Integer floor;

    public Building(ElevatorEngine elevatorEngine) {
        this.users = new HashSet<>();
        this.elevatorEngine = elevatorEngine;
        reset();
    }

    public Building addUser() {
        if (users.size() >= MAX_NUMBER_OF_USERS) {
            throw new IllegalStateException("can't add more than " + MAX_NUMBER_OF_USERS + " users");
        }

        User newUser = new User(elevatorEngine);
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
        Command command = elevatorEngine.nextCommand();

        if (!isValidCommand(command)) {
            elevatorEngine.reset();
            reset();
        } else {
            applyCommand(command);
        }

        return this;
    }

    private Boolean isValidCommand(Command command) {
        switch (command) {
            case CLOSE:
                return door == OPEN;
            case OPEN:
                return door == CLOSE;
            case DOWN:
                return door == CLOSE && floor > LOWER_FLOOR;
            case UP:
                return door == CLOSE && floor < HIGHER_FLOOR;
            default:
                return TRUE;
        }
    }

    private void reset() {
        floor = LOWER_FLOOR;
        door = CLOSE;
        users.clear();
    }

    private void applyCommand(Command command) {
        switch (command) {
            case CLOSE:
                door = CLOSE;
                break;
            case OPEN:
                door = OPEN;
                for (User user : users) {
                    user.elevatorIsOpen(floor);
                }
                break;
            case UP:
                floor++;
                break;
            case DOWN:
                floor--;
                break;
        }
    }

}
