package elevator;

import elevator.exception.ElevatorIsBrokenException;
import elevator.user.User;

import java.util.HashSet;
import java.util.Set;

import static elevator.Door.CLOSE;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.util.Collections.emptySet;

public class Elevator {

    private final Building building;
    private final Set<User> users;

    private Integer floor;
    private Door door;


    Elevator(Building building) {
        this.building = building;
        this.users = new HashSet<>();
        this.reset();
    }

    void notifyNewUser(User newUser) {
        if (door == Door.OPEN) {
            newUser.elevatorIsOpen(this, floor);
        }
    }

    private void notifyUsersThatElevatorIsAt(Integer floor) {
        for (User user : users) {
            user.elevatorIsAt(floor);
        }
    }

    void validateCommand(Command command) throws ElevatorIsBrokenException {
        switch (command) {
            case CLOSE:
                if (door != OPEN) {
                    throw new ElevatorIsBrokenException("can't close doors because they aren't opened");
                }
                break;
            case OPEN:
                if (door != CLOSE) {
                    throw new ElevatorIsBrokenException("can't open doors because they aren't closed");
                }
                break;
            case DOWN:
                if (door == OPEN) {
                    throw new ElevatorIsBrokenException("can't go down because doors are opened");
                }
                if (floor.equals(LOWER_FLOOR)) {
                    throw new ElevatorIsBrokenException("can't go down because current floor is the lowest floor");
                }
                break;
            case UP:
                if (door == OPEN) {
                    throw new ElevatorIsBrokenException("can't go up because doors are opened");
                }
                if (floor.equals(HIGHER_FLOOR)) {
                    throw new ElevatorIsBrokenException("can't go up because current floor is the highest floor");
                }
                break;
            case NOTHING:
                break;
        }
    }

    Set<User> applyCommand(Command command) throws ElevatorIsBrokenException {
        Set<User> doneUsers = emptySet();
        switch (command) {
            case CLOSE:
                door = CLOSE;
                break;
            case OPEN:
                door = OPEN;
                doneUsers = building.notifyUsersWithOpenElevatorDoor(floor);
                break;
            case UP:
                floor++;
                notifyUsersThatElevatorIsAt(floor);
                break;
            case DOWN:
                floor--;
                notifyUsersThatElevatorIsAt(floor);
                break;
            case NOTHING:
                break;
        }
        return doneUsers;
    }

    Integer floor() {
        return floor;
    }

    Door door() {
        return door;
    }

    public void userHasEntered(User user) {
        users.add(user);
    }

    public void userHasExited(User user) {
        users.remove(user);
    }

    public void reset() {
        floor = 0;
        door = CLOSE;
        users.clear();
    }
}
