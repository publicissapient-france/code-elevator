package elevator;

import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.InitializationStrategy;
import elevator.user.MaxNumberOfUsers;
import elevator.user.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public class Building {
    private final Set<User> users;
    private final ElevatorEngine elevatorEngine;
    private final Elevator elevator;
    private final MaxNumberOfUsers maxNumberOfUsers;

    public Building(ElevatorEngine elevatorEngine, MaxNumberOfUsers maxNumberOfUsers) {
        this.users = Collections.synchronizedSet(new HashSet<>());
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

    public long travelingUsers() {
        return users.stream().filter(user -> user.traveling()).count();
    }

    public Door door() {
        return elevator.door();
    }

    public Set<User> updateBuildingState() throws ElevatorIsBrokenException {
        Command command = elevatorEngine.nextCommand();
        elevator.validateCommand(command);
        users.forEach(User::tick);
        return elevator.applyCommand(command);
    }

    public void reset(String cause) {
        elevator.reset();
        users.clear();
        elevatorEngine.reset(cause);
    }

    Set<User> notifyUsersWithOpenElevatorDoor(Integer floor) {
        Set<User> doneUsers = users.stream()
                .peek(user -> user.elevatorIsOpen(elevator, floor))
                .filter(user -> user.done())
                .collect(toSet());
        users.removeAll(doneUsers);
        return doneUsers;
    }

    public Set<FloorState> floorStates() {
        return IntStream.range(LOWER_FLOOR, HIGHER_FLOOR + 1)
                .mapToObj(floor -> new FloorState(floor,
                        users.stream().filter(user -> user.waiting() && user.getInitialFloor().equals(floor)).count(),
                        users.stream().filter(user -> user.waiting() && user.getInitialFloor().equals(floor)).anyMatch(user -> user.getInitialDirection().equals(UP)),
                        users.stream().filter(user -> user.waiting() && user.getInitialFloor().equals(floor)).anyMatch(user -> user.getInitialDirection().equals(DOWN)),
                        users.stream().filter(user -> user.traveling()).anyMatch(user -> user.getFloorToGo().equals(floor))))
                .collect(toSet());
    }
}
