package elevator.engine.crazy;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;
import elevator.engine.naive.NaiveElevator;

import static java.lang.Math.random;

public class CrazyElevator implements ElevatorEngine {

    private ElevatorEngine underlyingElevator;

    public CrazyElevator() {
        underlyingElevator = new NaiveElevator();
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        doCrazy();
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        doCrazy();
        return this;
    }

    @Override
    public Command nextCommand() {
        doCrazy();
        return crazyCommand();
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        doCrazy();
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        doCrazy();
        return this;
    }

    @Override
    public ElevatorEngine reset(String cause) {
        underlyingElevator.reset(cause);
        return this;
    }

    private void doCrazy() {
        if (crazy()) {
            try {
                Long millisecondsToWait = new Double(random() * 1500).longValue();
                System.out.println("waiting for " + millisecondsToWait + "ms");
                Thread.sleep(millisecondsToWait);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        if (crazy()) {
            System.out.println(" throw an exception");
            throw new RuntimeException("crazy exception");
        }
    }

    private Command crazyCommand() {
        Command correctCommand = underlyingElevator.nextCommand();
        if (crazy()) {
            Command crazyCommand = Command.values()[new Double(random() * 5).intValue()];
            System.out.println("send " + crazyCommand + " instead of " + correctCommand);
            return crazyCommand;
        }
        return correctCommand;
    }

    private Boolean crazy() {
        Boolean crazy = random() * 9 > 8d;
        if (crazy) {
            System.out.print("CRAZY ");
        }
        return crazy;
    }

}
