package elevator.engine;

import elevator.Command;
import elevator.Direction;
import elevator.exception.ElevatorIsBrokenException;
import elevator.user.User;

public interface ElevatorEngine {

    Integer LOWER_FLOOR = 0;
    Integer HIGHER_FLOOR = 5;

    ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException;

    ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException;

    Command nextCommand() throws ElevatorIsBrokenException;

    ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException;

    ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException;

    ElevatorEngine reset(String cause) throws ElevatorIsBrokenException;

}
