package elevator.client;

import elevator.Direction;
import elevator.Door;

import java.util.Observable;
import java.util.Observer;

import static elevator.Command.*;
import static elevator.Direction.DOWN;
import static elevator.Direction.UP;

public class Elevator implements Observer, elevator.Elevator {

    private static final Integer MAX_FLOOR = 5;

    private final Commands commands = new Commands(0, MAX_FLOOR);

    private Integer floor = 0;
    private Door door = Door.CLOSE;

    public Door door() {
        return door;
    }

    public Integer floor() {
        return floor;
    }

    @Override
    public elevator.Elevator call(Integer atFloor, Direction to) {
        commands.add(new Command(atFloor, to));
        return this;
    }

    @Override
    public elevator.Elevator go(Integer floorToGo) {
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
        Command nextCommand = commands.get(floor);
        if (nextCommand == null) {
            if (door == Door.OPEN) {
                door = Door.CLOSE;
                return CLOSE;
            }
            return NOTHING;
        }

        if (door == Door.OPEN) {
            door = Door.CLOSE;
            return CLOSE;
        }

        Direction direction = nextCommand.getDirection(floor);

        if (nextCommand.equals(new Command(floor, direction))
                || (nextCommand.floor.equals(floor) && commands.commands().isEmpty())) {
            door = Door.OPEN;
            return OPEN;
        }

        if (direction == Direction.UP) {
            door = Door.CLOSE;
            floor++;
            return elevator.Command.UP;
        }

        if (direction == Direction.DOWN) {
            door = Door.CLOSE;
            floor--;
            return elevator.Command.DOWN;
        }

        throw new IllegalArgumentException();
    }

    @Override
    @Deprecated
    public void update(Observable clock, Object arg) {
        nextCommand();
    }

    @Override
    public String toString() {
        return "elevator " + door + " " + floor;
    }

}
