package elevator.server;

import elevator.user.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static elevator.Direction.DOWN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;

public class PlayerInfo implements Serializable {

    public final String pseudo;
    public final String email;
    public final int score;
    public final int averageScore;
    /**
     * @see PlayerInfo#floorState#peopleWaitingTheElevator
     */
    @Deprecated
    public final int[] peopleWaitingTheElevator;
    public final int elevatorAtFloor;
    public final int peopleInTheElevator;
    public final boolean doorIsOpen;
    public final String lastErrorMessage;
    public final String state;
    /**
     * @see PlayerInfo#floorState#upButtonState
     */
    @Deprecated
    public final boolean[] upButtonStateByFloor;
    /**
     * @see PlayerInfo#floorState#downButtonState
     */
    @Deprecated
    public final boolean[] downButtonStateByFloor;
    /**
     * @see PlayerInfo#floorState#floorButtonStateInElevator
     */
    @Deprecated
    public final boolean[] floorButtonStatesInElevator;
    public final FloorState[] floorState;
    private final Set<Integer> floorsWhereDownButtonIsLit = new HashSet<>();
    private final Set<Integer> floorsWhereUpButtonIsLit = new HashSet<>();

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
        floorState = initializeFloorStates(game, floorNb);
        floorButtonStatesInElevator = game.getFloorButtonStatesInElevator();
    }

    private FloorState[] initializeFloorStates(ElevatorGame game, Integer floorNb) {
        final FloorState[] floorStates = new FloorState[floorNb];
        for (Integer floor = LOWER_FLOOR; floor <= HIGHER_FLOOR; floor++) { // FIXME things goes wrong if LOWER_FLOOR is not zero
            floorStates[floor] = new FloorState(floor,
                    game.waitingUsersByFloors()[floor],
                    floorsWhereUpButtonIsLit.contains(floor),
                    floorsWhereDownButtonIsLit.contains(floor),
                    game.getFloorButtonStatesInElevator()[floor]);
        }
        return floorStates;
    }

    private void initializeBuildingButtonStates(Set<User> waitingUsers) {
        for (User user : waitingUsers) {
            if (user.getInitialDirection().equals(DOWN)) {
                downButtonStateByFloor[user.getInitialFloor()] = true;
                floorsWhereDownButtonIsLit.add(user.getInitialFloor());
            } else {
                upButtonStateByFloor[user.getInitialFloor()] = true;
                floorsWhereUpButtonIsLit.add(user.getInitialFloor());
            }
        }
    }

}
