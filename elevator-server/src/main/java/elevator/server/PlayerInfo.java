package elevator.server;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 5/29/13
 */
public class PlayerInfo {

    //TODO: optional ? private String name;

    public final String email;

    public final int score;

    public final int[] peopleWaitingTheElevator;

    public final int elevatorAtFloor;

    public final int peopleInTheElevator;

    public PlayerInfo(ElevatorGame game) {
        email = game.email.email;
        score = 0;
        peopleWaitingTheElevator = new int[]{0, 0, 0, 0};
        elevatorAtFloor = 0;
        peopleInTheElevator = 0;
    }
}
