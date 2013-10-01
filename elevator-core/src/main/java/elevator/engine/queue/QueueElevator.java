package elevator.engine.queue;

import elevator.Command;
import elevator.Direction;
import elevator.Door;
import elevator.user.User;
import elevator.engine.ElevatorEngine;

import java.util.ArrayDeque;
import java.util.Queue;

import static elevator.Command.*;

public class QueueElevator implements ElevatorEngine {

    final Queue<Integer> floorsToStop = new ArrayDeque<>();

    private Integer floor = LOWER_FLOOR;
    private Door door = Door.CLOSE;

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        floorsToStop.add(atFloor);
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        floorsToStop.add(floorToGo);
        return this;
    }

    @Override
    public Command nextCommand() {
        if (door.equals(Door.OPEN)) {
            door = Door.CLOSE;
            return CLOSE;
        }
        if (floorsToStop.isEmpty()) {
            return NOTHING;
        }
        if (floor.equals(floorsToStop.peek())) {
            floorsToStop.poll();
            door = Door.OPEN;
            return OPEN;
        }
        if (floor < floorsToStop.peek()) {
            floor++;
            return UP;
        }
        floor--;
        return DOWN;
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
    public ElevatorEngine reset(String cause) {
        floor = LOWER_FLOOR;
        door = Door.CLOSE;
        return this;
    }
}
