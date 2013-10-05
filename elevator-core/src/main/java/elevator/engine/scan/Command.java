package elevator.engine.scan;

public class Command {

    public final Integer floor;
    public final ElevatorDirection direction;

    public Command(Integer floor, ElevatorDirection direction) {
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

}
