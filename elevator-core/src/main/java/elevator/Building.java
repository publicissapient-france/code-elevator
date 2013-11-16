package elevator;

import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.InitializationStrategy;
import elevator.user.MaxNumberOfUsers;
import elevator.user.User;

import java.util.*;

import static elevator.Direction.DOWN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.util.Collections.unmodifiableSet;

public class Building {

    private final Set<User> users;
    private final ElevatorEngine elevatorEngine;
    private final Elevator elevator;
    private final MaxNumberOfUsers maxNumberOfUsers;

    public Building(ElevatorEngine elevatorEngine, MaxNumberOfUsers maxNumberOfUsers) {
        this.users = Collections.synchronizedSet(new HashSet<User>());
        this.elevatorEngine = elevatorEngine;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.elevator = new Elevator(this);
        reset("the elevator is at floor 0 and its doors are closed");
    }

    public Building addUser(InitializationStrategy strategy) throws ElevatorIsBrokenException {
        if (users.size() >= maxNumberOfUsers.value()) {
            return this;
        }

        User newUser = new User(elevatorEngine, strategy.create());
        elevator.notifyNewUser(newUser);
        users.add(newUser);
        return this;
    }

    public Set<User> users() {
        return unmodifiableSet(users);
    }

    public Integer floor() {
        return elevator.floor();
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

    public Door door() {
        return elevator.door();
    }

    public Set<User> updateBuildingState() throws ElevatorIsBrokenException {
        Command command = elevatorEngine.nextCommand();
        elevator.validateCommand(command);
        notifyUsersWithTick();
        return elevator.applyCommand(command);
    }

    public void reset(String cause) {
        elevator.reset();
        users.clear();
        elevatorEngine.reset(cause);
    }

    private void notifyUsersWithTick() {
        for (User user : users) {
            user.tick();
        }
    }

    Set<User> notifyUsersWithOpenElevatorDoor(Integer floor) {
        Set<User> doneUsers = new HashSet<>();
        for (User user : users) {
            user.elevatorIsOpen(elevator, floor);
            if (user.done()) {
                doneUsers.add(user);
            }
        }
        users.removeAll(doneUsers);
        return doneUsers;
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
