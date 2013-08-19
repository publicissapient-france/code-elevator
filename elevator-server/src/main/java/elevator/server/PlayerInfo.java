package elevator.server;

import java.io.Serializable;

public class PlayerInfo implements Serializable {

    public final String pseudo;
    public final String email;
    public final int score;
    public final int[] peopleWaitingTheElevator;
    public final int elevatorAtFloor;
    public final int peopleInTheElevator;
    public final boolean doorIsOpen;
    public final String lastErrorMessage;
    public final String state;

    public PlayerInfo(ElevatorGame game, Player player) {
        email = player.email;
        pseudo = player.pseudo;
        score = game.score.score;
        peopleWaitingTheElevator = game.waitingUsersByFloors();
        elevatorAtFloor = game.floor();
        peopleInTheElevator = game.travelingUsers();
        doorIsOpen = game.doorIsOpen();
        lastErrorMessage = game.lastErrorMessage;
        state = game.state.toString();
    }

}
