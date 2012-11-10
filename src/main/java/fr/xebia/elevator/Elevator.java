package fr.xebia.elevator;

import java.util.ArrayDeque;
import java.util.Deque;

import static fr.xebia.elevator.Instruction.open;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Elevator {

    private Integer currentFloor = 0;
    private Deque<Instruction> instructions = new ArrayDeque<>();
    private Boolean doorsOpen = FALSE;

    public int floor() {
        return currentFloor;
    }

    public Boolean isDoorsOpen() {
        return doorsOpen;
    }

    public Elevator tick() {
        Instruction currentInstruction = instructions.poll();
        if (currentInstruction != null) {
            currentInstruction.run(this);
        }
        return this;
    }

    public Elevator call(Integer fromFloor) {
        if (currentFloor.equals(fromFloor)) {
            instructions.offer(open);
        }

        return this;
    }

    void openDoors() {
        if (doorsOpen) {
            throw new IllegalStateException("Can't open doors because they are already open");
        }
        doorsOpen = TRUE;
    }

}
