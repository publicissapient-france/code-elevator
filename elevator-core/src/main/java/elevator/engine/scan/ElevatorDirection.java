package elevator.engine.scan;

import elevator.Direction;

public enum ElevatorDirection {

    UP, DOWN, NONE;

    public static ElevatorDirection elevatorDirection(Direction direction) {
        switch (direction) {
            case UP : return UP;
            case DOWN : return DOWN;
            default : throw new IllegalArgumentException();
        }
    }

}
