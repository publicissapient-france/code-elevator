package elevator;

public interface Elevator {

    public Elevator call(Integer atFloor, Direction to);

    public Elevator go(Integer floorToGo);

    public Command nextCommand();

}
