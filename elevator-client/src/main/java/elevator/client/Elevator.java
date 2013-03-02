package elevator.client;

import java.util.Observable;
import java.util.Observer;

import static elevator.client.Direction.DOWN;
import static elevator.client.Direction.UP;

public class Elevator implements Observer {

    private static final Integer maxFloor = 5;

    private final Commands commands = new Commands(0, maxFloor);

    private Integer floor = 0;
    private Direction direction;
    private Door door = Door.CLOSE;

    public Door door() {
        return door;
    }

    public Integer floor() {
        return floor;
    }

    public Elevator call(Integer atFloor, Direction to) {
        commands.add(new Command(atFloor, to));
        return this;
    }

    public Elevator go(Integer floorToGo) {
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
    public void update(Observable clock, Object arg) {
        Command nextCommand;
        if (direction == null) {
            nextCommand = commands.get(floor);
        } else {
            nextCommand = commands.get(floor, direction);
        }
        if (nextCommand == null) {
            door = Door.CLOSE;
            direction = null;
            return;
        }

        if (door == Door.OPEN) {
            door = Door.CLOSE;
            direction = null;
            return;
        }

        direction = nextCommand.getDirection(floor);

        if (nextCommand.equals(new Command(floor, direction)) || (nextCommand.floor.equals(floor) && commands.commands().isEmpty())) {
            door = Door.OPEN;
            direction = null;
            return;
        }

        if (direction == UP) {
            floor++;
            return;
        }

        if (direction == DOWN) {
            floor--;
        }
    }

    @Override
    public String toString() {
        return "elevator " + (direction == null ? door : direction) + " " + floor;
    }

}
