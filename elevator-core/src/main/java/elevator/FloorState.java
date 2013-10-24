package elevator;

public class FloorState {

    public final Integer floor;
    public final Integer waitingUsers;
    public final Boolean up;
    public final Boolean down;
    public final Boolean target;

    public FloorState(Integer floor, Integer waitingUsers, Boolean up, Boolean down, Boolean target) {
        this.floor = floor;
        this.waitingUsers = waitingUsers;
        this.up = up;
        this.down = down;
        this.target = target;
    }

}
