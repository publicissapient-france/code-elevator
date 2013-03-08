package elevator.server;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

class StartedElevatorServer {

    private Set<ElevatorGame> elevatorGames = new HashSet<>();

    public StartedElevatorServer addElevatorGame(Email email) {
        ElevatorGame elevatorGame = new ElevatorGame(email);
        if (elevatorGames.contains(elevatorGame)) {
            throw new IllegalStateException("a game with email " + email + " has already have been added");
        }
        elevatorGames.add(elevatorGame);
        return this;
    }

    public Set<ElevatorGame> elevatorGames() {
        return unmodifiableSet(elevatorGames);
    }

}
