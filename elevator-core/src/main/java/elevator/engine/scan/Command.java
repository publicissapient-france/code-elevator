package elevator.engine.scan;

import elevator.Direction;

public class Command {

    final Integer floor;
    final Direction direction;

    Command(Integer floor, Direction direction) {
        if (floor == null || direction == null) {
            throw new NullPointerException();
        }
        this.floor = floor;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        return direction == command.direction && floor.equals(command.floor);
    }

    @Override
    public int hashCode() {
        int result = floor.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return floor + " " + direction;
    }

    public Direction getDirection(Integer floor) {
        if (this.floor.equals(floor)) {
            return direction;
        } else if (this.floor < floor) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

}
