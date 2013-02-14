package elevator;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.ElevatorState.CLOSE;
import static elevator.ElevatorState.OPEN;

public class Elevator implements Observer {

    private static final Integer maxStage = 5;

    private static final Logger LOGGER = Logger.getLogger(Elevator.class.getName());

    private final Commands commands = new Commands(0, maxStage);
    private Integer stage = 0;
    private Direction direction;
    private ElevatorState state = CLOSE;

    public ElevatorState getState() {
        return state;
    }

    public Integer getStage() {
        return stage;
    }

    public Elevator call(Integer atStage, Direction to) {
        LOGGER.entering(this.getClass().getName(), "call", new Object[]{atStage, to});
        commands.add(new Command(atStage, to));
        return this;
    }

    public Elevator go(Integer stageToGo) {
        LOGGER.entering(this.getClass().getName(), "go", stageToGo);
        final Direction direction;
        if (stage > stageToGo) {
            direction = DOWN;
        } else {
            direction = UP;
        }
        call(stageToGo, direction);
        return this;
    }

    @Override
    public void update(Observable clock, Object arg) {
        LOGGER.entering(this.getClass().getName(), "update", new Object[]{clock, arg});
        Command nextCommand;
        if (direction == null) {
            nextCommand = commands.get(stage);
        } else {
            nextCommand = commands.get(stage, direction);
        }
        if (nextCommand == null) {
            state = CLOSE;
            direction = null;
            LOGGER.finer("closing or does nothing");
            return;
        }

        if (state == OPEN) {
            state = CLOSE;
            direction = null;
            LOGGER.finer("closing");
            return;
        }

        if (nextCommand.stage.equals(stage)) {
            direction = nextCommand.direction;
        } else if (nextCommand.stage < stage) {
            direction = DOWN;
        } else {
            direction = UP;
        }

        if (nextCommand.equals(new Command(stage, direction)) || (nextCommand.stage.equals(stage) && commands.commands().isEmpty())) {
            state = OPEN;
            direction = null;
            LOGGER.finer("open");
            return;
        }

        if (direction == UP) {
            stage++;
            direction = UP;
            LOGGER.finer("up");
            return;
        }

        if (direction == DOWN) { // stage > nextStage.stage
            stage--;
            direction = DOWN;
            LOGGER.finer("down");
        }
    }

    @Override
    public String toString() {
        return "elevator " + (direction == null ? state : direction) + " " + stage;
    }

}
