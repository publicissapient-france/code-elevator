package elevator.engine;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.exception.ElevatorIsBrokenException;

public interface ElevatorEngine {

    public final static Integer LOWER_FLOOR = 0;
    public final static Integer HIGHER_FLOOR = 5;

    public ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException;

    public ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException;

    public Command nextCommand() throws ElevatorIsBrokenException;

    public ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException;

    public ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException;

    public ElevatorEngine reset(String cause) throws ElevatorIsBrokenException;

}
