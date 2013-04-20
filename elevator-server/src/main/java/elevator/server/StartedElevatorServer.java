package elevator.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class StartedElevatorServer {

    private Set<ElevatorGame> elevatorGames = new HashSet<>();

    public StartedElevatorServer addElevatorGame(Email email, URL server) throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(email, server);
        if (elevatorGames.contains(elevatorGame)) {
            throw new IllegalStateException("a game with email " + email + " has already have been added");
        }
        elevatorGames.add(elevatorGame);
        return this;
    }

    public Set<Email> emails() {
        Set<Email> emails = new HashSet<>(elevatorGames.size());
        for (ElevatorGame elevatorGame : elevatorGames) {
            emails.add(elevatorGame.email);
        }
        return unmodifiableSet(emails);
    }

}
