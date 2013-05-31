package elevator.server;

import elevator.*;

import java.net.MalformedURLException;
import java.net.URL;

class ElevatorGame implements ClockListener {

    private static final String HTTP = "http";

    final Player player;

    private final Clock clock;
    private final HTTPElevator elevatorEngine;
    private final Building building;

    ElevatorGame(Player player, URL url, Clock clock) throws MalformedURLException {
        if (!HTTP.equals(url.getProtocol())) {
            throw new IllegalArgumentException("http is the only supported protocol");
        }
        this.player = player;
        this.elevatorEngine = new HTTPElevator(url, clock.EXECUTOR_SERVICE);
        this.building = new Building(elevatorEngine);
        this.clock = clock;
    }

    ElevatorGame start() {
        clock.addClockListener(this);
        elevatorEngine.reset();
        return this;
    }

    ElevatorGame stop() {
        clock.removeClockListener(this);
        return this;
    }

    int floor() {
        return building.floor();
    }

    Score score() {
        return elevatorEngine.getScore();
    }

    @Override
    public ClockListener onTick() {
        if (elevatorEngine.hasTransportError()) {
            stop();
            return this;
        }
        building.addUser();
        building.updateBuildingState();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorGame elevatorGame = (ElevatorGame) o;

        return player.equals(elevatorGame.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    public PlayerInfo getPlayerInfo() {
        PlayerInfo playerInfo = new PlayerInfo();

        playerInfo.email = player.email;
        playerInfo.pseudo = player.pseudo;
        playerInfo.score = score().score;
        for (User user: building.users()) {
            if (user.state() == User.State.WAITING) {
                playerInfo.peopleWaitingTheElevator[user.getFloor()] = playerInfo.peopleWaitingTheElevator[user.getFloor()] + 1;
            }
            if (user.state() == User.State.TRAVELLING) {
                playerInfo.peopleInTheElevator++;
            }
        }
        playerInfo.elevatorAtFloor = building.floor();
        playerInfo.door = building.door();
        return playerInfo;
    }

}
