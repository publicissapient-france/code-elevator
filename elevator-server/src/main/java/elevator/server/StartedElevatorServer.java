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

    public StartedElevatorServer addElevatorGame(Player player, URL server) throws MalformedURLException {
        ElevatorGame elevatorGame = new ElevatorGame(player, server, clock);
        if (elevatorGames.contains(elevatorGame)) {
            throw new IllegalStateException("a game with player " + player + " has already have been added");
        }
        elevatorGames.add(elevatorGame);
        elevatorGame.start();
        return this;
    }

    public Set<Player> emails() {
        Set<Player> emails = new HashSet<>(elevatorGames.size());
        for (ElevatorGame elevatorGame : elevatorGames) {
            emails.add(elevatorGame.player);
        }
        return unmodifiableSet(emails);
    }

    public StartedElevatorServer removeElevatorGame(Player email) throws MalformedURLException {
        for (ElevatorGame elevatorGame : elevatorGames) {
            if (elevatorGame.player.equals(email)) {
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
