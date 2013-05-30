package elevator.server;

import java.io.Serializable;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 5/29/13
 */
public class PlayerInfo implements Serializable {
    private static final long serialVersionUID = -6875006554350182431L;

    //TODO: optional ? private String name;

    public final String email;

    public final int score;

    public final int[] peopleWaitingTheElevator;

    public final int elevatorAtFloor;

    public final int peopleInTheElevator;

    public PlayerInfo(ElevatorGame game) {
        email = game.email.email;
        score = game.score().score;
        peopleWaitingTheElevator = new int[]{0, 0, 0, 0};
        elevatorAtFloor = game.floor();
        peopleInTheElevator = 0;
    }
}
