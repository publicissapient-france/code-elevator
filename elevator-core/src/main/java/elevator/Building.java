package elevator;

import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.InitializationStrategy;
import elevator.user.MaxNumberOfUsers;
import elevator.user.User;

import java.util.*;

import static elevator.Direction.DOWN;
import static elevator.Door.CLOSE;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class Building {

    private final Set<User> users;
    private final ElevatorEngine elevatorEngine;
    private final MaxNumberOfUsers maxNumberOfUsers;

    private Door door;
    private Integer floor;

    public Building(ElevatorEngine elevatorEngine, MaxNumberOfUsers maxNumberOfUsers) {
        this.users = Collections.synchronizedSet(new HashSet<User>());
        this.elevatorEngine = elevatorEngine;
        this.maxNumberOfUsers = maxNumberOfUsers;
        reset();
    }

    public synchronized Building addUser(InitializationStrategy strategy) throws ElevatorIsBrokenException {
        if (users.size() >= maxNumberOfUsers.value()) {
            return this;
        }

        User newUser = new User(elevatorEngine, strategy);
        if (door == Door.OPEN) {
            newUser.elevatorIsOpen(floor);
        } else {
            newUser.elevatorIsAt(floor);
        }
        users.add(newUser);
        return this;
    }

    public synchronized Set<User> users() {
        return unmodifiableSet(users);
    }

    public Integer floor() {
        return floor;
    }

    public synchronized int travelingUsers() {
        int count = 0;

        for (User user : users) {
            if (user.traveling()) {
                count++;
            }
        }

        return count;
    }

    public Door door() {
        return door;
    }

    public Set<User> updateBuildingState() throws ElevatorIsBrokenException {
        Command command = elevatorEngine.nextCommand();
        validateCommand(command);
        return applyCommand(command);
    }

    private void validateCommand(Command command) throws ElevatorIsBrokenException {
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

    public synchronized void reset() {
        floor = LOWER_FLOOR;
        door = CLOSE;
        users.clear();
    }

    private synchronized Set<User> applyCommand(Command command) throws ElevatorIsBrokenException {
        Set<User> doneUsers = emptySet();
        notifyUsers();
        switch (command) {
            case CLOSE:
                door = CLOSE;
                break;
            case OPEN:
                door = OPEN;
                doneUsers = new HashSet<>();
                for (User user : users) {
                    user.elevatorIsOpen(floor);
                    if (user.done()) {
                        doneUsers.add(user);
                    }
                }
                users.removeAll(doneUsers);
                break;
            case UP:
                floor++;
                notifyUsers(floor);
                break;
            case DOWN:
                floor--;
                notifyUsers(floor);
                break;
            case NOTHING:
                break;
        }
        return doneUsers;
    }

    private synchronized void notifyUsers() {
        for (User user : users) {
            user.tick();
        }
    }

    private synchronized void notifyUsers(Integer floor) {
        for (User user : users) {
            user.elevatorIsAt(floor);
        }
    }

    public Set<FloorState> floorStates() {
        final Set<Integer> floorsWhereDownButtonIsLit = new HashSet<>();
        final Set<Integer> floorsWhereUpButtonIsLit = new HashSet<>();
        final Set<Integer> targetedFloors = new HashSet<>();
        final Map<Integer, Integer> waitingUserByFloorSet = new HashMap<>();
        for (Integer floor = ElevatorEngine.LOWER_FLOOR; floor <= ElevatorEngine.HIGHER_FLOOR; floor++) {
            waitingUserByFloorSet.put(floor, 0);
        }
        for (User user : users) {
            if (user.waiting()) {
                if (user.getInitialDirection().equals(DOWN)) {
                    floorsWhereDownButtonIsLit.add(user.getInitialFloor());
                } else {
                    floorsWhereUpButtonIsLit.add(user.getInitialFloor());
                }
                waitingUserByFloorSet.put(user.getInitialFloor(), waitingUserByFloorSet.get(user.getInitialFloor()) + 1);
            } else if (user.traveling()) {
                targetedFloors.add(user.getFloorToGo());
            }
        }

        final Set<FloorState> floorStates = new HashSet<>();
        for (Integer floor = LOWER_FLOOR; floor <= HIGHER_FLOOR; floor++) {
            floorStates.add(new FloorState(floor,
                    waitingUserByFloorSet.get(floor),
                    floorsWhereUpButtonIsLit.contains(floor),
                    floorsWhereDownButtonIsLit.contains(floor),
                    targetedFloors.contains(floor)));
        }
        return floorStates;
    }

}
