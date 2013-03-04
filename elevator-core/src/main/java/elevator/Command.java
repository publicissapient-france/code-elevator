package elevator;

public enum Command {

    UP, DOWN, OPEN, CLOSE, NOTHING,;

    public static Command commandFrom(Direction direction) {
        switch (direction) {
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            default:
                throw new IllegalArgumentException();
        }
    }

}
