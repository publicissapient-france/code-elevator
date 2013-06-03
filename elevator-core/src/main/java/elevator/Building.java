package elevator;

import elevator.engine.ElevatorEngine;

import java.util.HashSet;
import java.util.Set;

import static elevator.Door.CLOSE;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.unmodifiableSet;

public class Building {

    static final Integer MAX_NUMBER_OF_USERS = 10;

    private final Set<User> users;
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
            return this;
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

    public int travelingUsers() {
        int count = 0;

        for (User user : users) {
            if (user.traveling()) {
                count++;
            }
        }

        return count;
    }

    public int[] waitingUsersByFloors() {
        int[] count = new int[ElevatorEngine.HIGHER_FLOOR - ElevatorEngine.LOWER_FLOOR + 1];

        for (int i = ElevatorEngine.LOWER_FLOOR; i <= ElevatorEngine.HIGHER_FLOOR; i++) {
            count[i] = 0;
            for (User user : users) {
                if (user.waiting() && user.at(i)) {
                    count[i]++;
                }
            }
        }

        return count;
    }

    public Door door() {
        return door;
    }

    public Building updateBuildingState() {
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
        if (command == null) {
            return FALSE;
        }
        switch (command) {
            case CLOSE:
                return door == OPEN;
            case OPEN:
                return door == CLOSE;
            case DOWN:
                return door == CLOSE && floor > LOWER_FLOOR;
            case UP:
                return door == CLOSE && floor < HIGHER_FLOOR;
            case NOTHING:
                return TRUE;
            default:
                return FALSE;
        }
    }

    private void reset() {
        floor = LOWER_FLOOR;
        door = CLOSE;
        users.clear();
    }

    private void applyCommand(Command command) {
        notifyUsers();
        switch (command) {
            case CLOSE:
                door = CLOSE;
                break;
            case OPEN:
                door = OPEN;
                Set<User> usersToDelete = new HashSet<>();
                for (User user : users) {
                    user.elevatorIsOpen(floor);
                    if (user.done()) {
                        usersToDelete.add(user);
                    }
                }
                users.removeAll(usersToDelete);
                break;
            case UP:
                floor++;
                notifyUsers(floor);
                break;
            case DOWN:
                floor--;
                notifyUsers(floor);
                break;
        }
    }

    private void notifyUsers() {
        for (User user : users) {
            user.tick();
        }
    }

    private void notifyUsers(Integer floor) {
        for (User user : users) {
            if (user.traveling()) {
                user.setCurrentFloor(floor);
            }
        }
    }

}
