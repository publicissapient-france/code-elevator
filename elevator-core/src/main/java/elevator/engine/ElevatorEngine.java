package elevator.engine;

import elevator.Command;
import elevator.Direction;

public interface ElevatorEngine {

    public ElevatorEngine call(Integer atFloor, Direction to);

    public ElevatorEngine go(Integer floorToGo);

    public Command nextCommand();

}
