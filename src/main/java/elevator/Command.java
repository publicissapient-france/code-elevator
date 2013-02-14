package elevator;

public class Command {

    final Integer stage;
    final Direction direction;

    Command(Integer stage, Direction direction) {
        if (stage == null || direction == null) {
            throw new NullPointerException();
        }
        this.stage = stage;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        return direction == command.direction && stage.equals(command.stage);
    }

    @Override
    public int hashCode() {
        int result = stage.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return stage + " " + direction;
    }
}
