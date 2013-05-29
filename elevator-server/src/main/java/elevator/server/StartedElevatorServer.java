package elevator.server;

import elevator.Clock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import static java.util.Collections.unmodifiableSet;
import static java.util.concurrent.TimeUnit.SECONDS;

class StartedElevatorServer {

    private Set<ElevatorGame> elevatorGames = new HashSet<>();
    private final Clock clock = new Clock();

    StartedElevatorServer() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(clock::tick, 0, 1, SECONDS);
    }

    public StartedElevatorServer addElevatorGame(Email email, URL server) throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(email, server, clock);
        if (elevatorGames.contains(elevatorGame)) {
            throw new IllegalStateException("a game with email " + email + " has already have been added");
        }
        elevatorGames.add(elevatorGame);
        elevatorGame.start();
        return this;
    }

    public Set<Email> emails() {
        Set<Email> emails = new HashSet<>(elevatorGames.size());
        for (ElevatorGame elevatorGame : elevatorGames) {
            emails.add(elevatorGame.email);
        }
        return unmodifiableSet(emails);
    }

    public StartedElevatorServer removeElevatorGame(Email email) throws MalformedURLException {
        for (ElevatorGame elevatorGame : elevatorGames) {
            if (elevatorGame.email.equals(email)) {
                elevatorGame.stop();
                elevatorGames.remove(elevatorGame);
            }
        }
        return this;
    }

    public Collection<ElevatorGame> getUnmodifiableElevatorGames() {
        return Collections.unmodifiableCollection(elevatorGames);
    }
}
