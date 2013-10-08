package elevator.engine.scan;

import elevator.Direction;
import elevator.Door;
import elevator.engine.ElevatorEngine;
import elevator.user.User;

import static elevator.Command.*;
import static elevator.engine.scan.ElevatorDirection.NONE;
import static elevator.engine.scan.ElevatorDirection.elevatorDirection;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ScanElevator implements ElevatorEngine {

    private final Commands commands = new Commands(LOWER_FLOOR, HIGHER_FLOOR);

    private Integer floor = LOWER_FLOOR;
    private Door door = Door.CLOSE;
    private Boolean justClosing = FALSE;
    private ElevatorDirection elevatorDirection = ElevatorDirection.UP;

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        call(atFloor, elevatorDirection(to));
        return this;
    }

    private void call(Integer atFloor, ElevatorDirection to) {
        commands.add(new Command(atFloor, to));
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        final ElevatorDirection direction;
        if (floor.equals(floorToGo)) {
            direction = NONE;
        } else if (floor > floorToGo) {
            direction = !floorToGo.equals(LOWER_FLOOR) ? ElevatorDirection.DOWN : ElevatorDirection.UP;
        } else {
            direction = !floorToGo.equals(HIGHER_FLOOR) ? ElevatorDirection.UP : ElevatorDirection.DOWN;
        }
        call(floorToGo, direction);
        return this;
    }

    @Override
    public elevator.Command nextCommand() {
        if (door == Door.OPEN) {
            commands.clear(floor, elevatorDirection);
            door = Door.CLOSE;
            justClosing = TRUE;
            return CLOSE;
        }

        Command nextCommand = commands.get(floor, elevatorDirection);

        if (nextCommand == null) {
            justClosing = FALSE;
            elevatorDirection = NONE;
            return NOTHING;
        }

        if (nextCommand.floor.equals(floor) && (elevatorDirection == NONE || nextCommand.direction.equals(elevatorDirection) || noCommandToDirection(nextCommand.direction))) {
            if (justClosing) {
                Command nextCommandOutsideGivenFloor = commands.next(floor);
                if (nextCommandOutsideGivenFloor != null) {
                    nextCommand = nextCommandOutsideGivenFloor;
                } else {
                    justClosing = FALSE;
                }
            }
            if (!justClosing) {
                door = Door.OPEN;
                return OPEN;
            }
        }

        justClosing = FALSE;

        if (nextCommand.floor > floor) {
            floor++;
            elevatorDirection = !floor.equals(HIGHER_FLOOR) ? ElevatorDirection.UP : ElevatorDirection.DOWN;
            return UP;
        }

        if (nextCommand.floor < floor) {
            floor--;
            elevatorDirection = !floor.equals(LOWER_FLOOR) ? ElevatorDirection.DOWN : ElevatorDirection.UP;
            return DOWN;
        }

        throw new IllegalStateException();
    }

    private Boolean noCommandToDirection(ElevatorDirection direction) {
        Command nextCommand = commands.next(floor);
        return nextCommand == null
                || ((direction == ElevatorDirection.UP || floor.equals(HIGHER_FLOOR)) && floor < nextCommand.floor)
                || ((direction == ElevatorDirection.DOWN || floor.equals(LOWER_FLOOR)) && floor > nextCommand.floor);
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        return this;
    }

    @Override
    public ScanElevator reset(String cause) {
        door = Door.CLOSE;
        floor = LOWER_FLOOR;
        justClosing = FALSE;
        elevatorDirection = ElevatorDirection.UP;
        commands.reset();
        return this;
    }

    @Override
    public String toString() {
        return "elevator " + door + " " + floor;
    }

}
