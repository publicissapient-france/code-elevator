package elevator;

import elevator.Direction;
import elevator.User;

import java.io.Serializable;

public class WaitingUser implements Serializable
{
    private final int floorNum;
    private final Direction direction;

    public WaitingUser(User user) {
        this.floorNum = user.getInitialFloor();
        this.direction = user.getInitialDirection();
    }

    public int getFloorNum(){
        return floorNum;
    }

    public Direction getDesiredDirection(){
        return direction;
    }
}
