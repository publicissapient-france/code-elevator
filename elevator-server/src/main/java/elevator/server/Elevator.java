package elevator.server;

import elevator.Command;
import elevator.Direction;

public class Elevator implements elevator.Elevator {

    @Override
    public elevator.Elevator call(Integer atFloor, Direction to) {
        throw new RuntimeException();
    }

    @Override
    public elevator.Elevator go(Integer floorToGo) {
        throw new RuntimeException();
    }

    @Override
    public Command nextCommand() {
        throw new RuntimeException();
    }

}
