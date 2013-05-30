package elevator.engine;

import elevator.Command;
import elevator.Direction;
import elevator.User;

public interface ElevatorEngine {

    public final Integer LOWER_FLOOR = 0;
    public final Integer HIGHER_FLOOR = 5;

    public ElevatorEngine call(Integer atFloor, Direction to);

    public ElevatorEngine go(Integer floorToGo);

    public Command nextCommand();

    public ElevatorEngine userHasEntered(User user);

    public ElevatorEngine userHasExited(User user);

    public ElevatorEngine reset();

}
