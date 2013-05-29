package elevator.server;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 5/29/13
 */
public class PlayerInfo {

    //TODO: optional ? private String name;

    public transient final Email email;

    private int score;

    private int[] peopleWaitingTheElevator;

    private int elevatorAtFloor;

    private int peopleInTheElevator;

    public PlayerInfo(Email email) {
        this.email = email;
        score = 0;
        peopleWaitingTheElevator = new int[]{0, 0, 0, 0};
        elevatorAtFloor = 0;
        peopleInTheElevator = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfo that = (PlayerInfo) o;

        return email.equals(that.email);
    }

    public String getEmail() {
        return email.toString();
    }

    public int getScore() {
        return score;
    }

    public int[] getPeopleWaitingTheElevator() {
        return peopleWaitingTheElevator;
    }

    public int getElevatorAtFloor() {
        return elevatorAtFloor;
    }

    public int getPeopleInTheElevator() {
        return peopleInTheElevator;
    }

    public void loose() {
        this.score -= 1000;
    }

    public void onepoint() {
        this.score++;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
