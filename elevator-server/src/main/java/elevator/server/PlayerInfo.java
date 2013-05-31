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

    public String pseudo = "";

    public String email = "";

    public int score = 0;

    public int[] peopleWaitingTheElevator = new int[ElevatorEngine.HIGHER_FLOOR+1];

    public int elevatorAtFloor = 0;

    public int peopleInTheElevator = 0;

    public Door door = Door.CLOSE;

    public PlayerInfo() {
        for (int i = 0; i < peopleWaitingTheElevator.length; i++) {
            peopleWaitingTheElevator[i] = 0;
        }
    }

}
