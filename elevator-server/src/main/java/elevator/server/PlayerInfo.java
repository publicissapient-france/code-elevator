package elevator.server;

import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;

import java.io.Serializable;
import java.util.Set;

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
    public boolean[] upButtonStateByFloor;
    public boolean[] downButtonStateByFloor;
    public boolean[] floorButtonStatesInElevator;

    public PlayerInfo(ElevatorGame game, Player player) {
        email = player.email;
        pseudo = player.pseudo;
        score = game.score();
        peopleWaitingTheElevator = game.waitingUsersByFloors();
        elevatorAtFloor = game.floor();
        peopleInTheElevator = game.travelingUsers();
        doorIsOpen = game.doorIsOpen();
        lastErrorMessage = game.lastErrorMessage;
        state = game.state.toString();

        initializeBuildingButtonStates(game.waitingUsers());
        floorButtonStatesInElevator = game.getFloorButtonStatesInElevator();
    }

    private void initializeBuildingButtonStates(Set<User> waitingUsers) {
        int floorNb = ElevatorEngine.HIGHER_FLOOR - ElevatorEngine.LOWER_FLOOR + 1;
        upButtonStateByFloor = new boolean[floorNb];
        downButtonStateByFloor = new boolean[floorNb];

        for (User user : waitingUsers) {
            boolean[] states = user.getInitialDirection().equals(Direction.DOWN) ? downButtonStateByFloor : upButtonStateByFloor;

            states[user.getInitialFloor()] = true;
        }
    }

}
