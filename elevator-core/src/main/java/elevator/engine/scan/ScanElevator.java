package elevator.engine.scan;

import elevator.Direction;
import elevator.Door;
import elevator.User;
import elevator.engine.ElevatorEngine;

import static elevator.Command.*;
import static elevator.Direction.DOWN;
import static elevator.Direction.UP;

public class ScanElevator implements ElevatorEngine {

    private final Commands commands = new Commands(LOWER_FLOOR, HIGHER_FLOOR);

    private Integer floor = LOWER_FLOOR;
    private Door door = Door.CLOSE;

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        commands.add(new Command(atFloor, to));
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        final Direction direction;
        if (floor > floorToGo) {
            direction = DOWN;
        } else {
            direction = UP;
        }
        call(floorToGo, direction);
        return this;
    }

    @Override
    public elevator.Command nextCommand() {
        if (door == Door.OPEN) {
            door = Door.CLOSE;
            return CLOSE;
        }

        Command nextCommand = commands.get(floor);
        if (nextCommand == null) {
            return NOTHING;
        }

        Direction direction = nextCommand.getDirection(floor);

        if (nextCommand.equals(new Command(floor, direction))
                || (nextCommand.floor.equals(floor) && commands.commands().isEmpty())) {
            door = Door.OPEN;
            return OPEN;
        }

        if (direction == Direction.UP) {
            floor++;
            return elevator.Command.UP;
        }

        if (direction == Direction.DOWN) {
            floor--;
            return elevator.Command.DOWN;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        return this;
    }

    @Override
    public ScanElevator reset(String cause) {
        door = Door.CLOSE;
        floor = LOWER_FLOOR;
        return this;
    }

    @Override
    public String toString() {
        return "elevator " + door + " " + floor;
    }

}
