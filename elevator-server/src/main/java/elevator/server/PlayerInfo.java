package elevator.server;

import elevator.Door;
import elevator.engine.ElevatorEngine;

import java.io.Serializable;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 5/29/13
 */
public class PlayerInfo implements Serializable {
    private static final long serialVersionUID = -6875006554350182431L;

    public final String pseudo;

    public final String email;

    public final int score;

    public final int[] peopleWaitingTheElevator;

    public final int elevatorAtFloor;

    public final int peopleInTheElevator;

    public PlayerInfo(ElevatorGame game, Player player) {
        email = player.email;
        pseudo = player.pseudo;
        score = game.score().score;
        peopleWaitingTheElevator = game.waitingUsersByFloors();
        elevatorAtFloor = game.floor();
        peopleInTheElevator = game.travelingUsers();
    }

}
