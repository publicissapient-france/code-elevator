package elevator.server;

import elevator.Clock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

import static java.util.Collections.unmodifiableSet;
import static java.util.concurrent.TimeUnit.SECONDS;

class StartedElevatorServer {

    private final Map<String, ElevatorGame> elevatorGames = new TreeMap<>();
    private final Clock clock = new Clock();

    StartedElevatorServer() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(clock::tick, 0, 1, SECONDS);
    }

    public StartedElevatorServer addElevatorGame(Player player, URL server) throws MalformedURLException {
        if (elevatorGames.containsKey(player.email)) {
            throw new IllegalStateException("a game with player " + player + " has already have been added");
        }
        ElevatorGame elevatorGame = new ElevatorGame(player, server, clock);
        elevatorGame.start();
        elevatorGames.put(player.email, elevatorGame);
        return this;
    }

    public Set<Player> players() {
        Set<Player> players = new HashSet<>(elevatorGames.size());
        for (ElevatorGame elevatorGame : elevatorGames.values()) {
            players.add(elevatorGame.player);
        }
        return unmodifiableSet(players);
    }

    public StartedElevatorServer removeElevatorGame(String email) {
        if (elevatorGames.containsKey(email)) {
            ElevatorGame game = elevatorGames.get(email);
            game.stop();
            elevatorGames.remove(email);
        }
        return this;
    }

    public PlayerInfo getPlayerInfo(String email) throws PlayerNotFoundException {

        if (!elevatorGames.containsKey(email)) {
            throw new PlayerNotFoundException("Player not found");
        }
        return elevatorGames.get(email).getPlayerInfo();
    }

    public Collection<ElevatorGame> getUnmodifiableElevatorGames() {
        return Collections.unmodifiableCollection(elevatorGames.values());
    }

}
