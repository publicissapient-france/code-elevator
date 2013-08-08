package elevator.engine.crazy;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;
import elevator.engine.naive.NaiveElevator;
import elevator.logging.ElevatorLogger;

import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.lang.String.format;
import static java.util.logging.Level.WARNING;

public class CrazyElevator implements ElevatorEngine {

    private final Logger logger;
    private ElevatorEngine underlyingElevator;

    public CrazyElevator() {
        logger = new ElevatorLogger("CrazyElevator").logger();
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
                logger.info(format("waiting for %dms", millisecondsToWait));
                Thread.sleep(millisecondsToWait);
            } catch (InterruptedException e) {
                logger.log(WARNING, e.getMessage(), e);
            }
        }
        if (crazy()) {
            logger.info("throw an exception");
            throw new RuntimeException("crazy exception");
        }
    }

    private Command crazyCommand() {
        Command correctCommand = underlyingElevator.nextCommand();
        if (crazy()) {
            Command crazyCommand = Command.values()[new Double(random() * 5).intValue()];
            logger.info(format("send %s instead of %s", crazyCommand, correctCommand));
            return crazyCommand;
        }
        return correctCommand;
    }

    private Boolean crazy() {
        return random() * 9 > 8d;
    }

}
