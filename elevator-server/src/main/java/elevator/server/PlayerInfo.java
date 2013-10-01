package elevator.server;

import elevator.user.User;

import java.io.Serializable;
import java.util.Set;

import static elevator.Direction.DOWN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;

public class PlayerInfo implements Serializable {

    public final String pseudo;
    public final String email;
    public final int score;
    public final int averageScore;
    public final int[] peopleWaitingTheElevator;
    public final int elevatorAtFloor;
    public final int peopleInTheElevator;
    public final boolean doorIsOpen;
    public final String lastErrorMessage;
    public final String state;
    public final boolean[] upButtonStateByFloor;
    public final boolean[] downButtonStateByFloor;
    public final boolean[] floorButtonStatesInElevator;


    public PlayerInfo(ElevatorGame game, Player player) {
        final Integer floorNb = HIGHER_FLOOR - LOWER_FLOOR + 1;

        email = player.email;
        pseudo = player.pseudo;
        score = game.score();
        averageScore = game.averageScore();
        peopleWaitingTheElevator = game.waitingUsersByFloors();
        elevatorAtFloor = game.floor();
        peopleInTheElevator = game.travelingUsers();
        doorIsOpen = game.doorIsOpen();
        lastErrorMessage = game.lastErrorMessage;
        state = game.state.toString();
        upButtonStateByFloor = new boolean[floorNb];
        downButtonStateByFloor = new boolean[floorNb];
        initializeBuildingButtonStates(game.waitingUsers());
        floorButtonStatesInElevator = game.getFloorButtonStatesInElevator();
    }

    private void initializeBuildingButtonStates(Set<User> waitingUsers) {
        for (User user : waitingUsers) {
            if (user.getInitialDirection().equals(DOWN)) {
                downButtonStateByFloor[user.getInitialFloor()] = true;
            } else {
                upButtonStateByFloor[user.getInitialFloor()] = true;
            }
        }
    }

}
