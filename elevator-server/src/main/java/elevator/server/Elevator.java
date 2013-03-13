package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;

public class Elevator implements ElevatorEngine {

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        throw new RuntimeException();
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        throw new RuntimeException();
    }

    @Override
    public Command nextCommand() {
        throw new RuntimeException();
    }

}
