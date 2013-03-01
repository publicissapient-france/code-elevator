package elevator.server;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

class StartedElevatorServer {
    private Set<ElevatorGame> elevatorGames = new HashSet<>();

    public StartedElevatorServer addElevatorGame(Elevator elevator) {
        ElevatorGame elevatorGame = new ElevatorGame(elevator);
        if (elevatorGames.contains(elevatorGame)) {
            throw new IllegalStateException("a game with email " + elevator.email + " has already have been added");
        }
        elevatorGames.add(elevatorGame);
        return this;
    }

    public Set<ElevatorGame> elevatorGames() {
        return unmodifiableSet(elevatorGames);
    }
}
