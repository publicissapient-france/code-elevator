package elevator;

import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Logger;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.ElevatorState.CLOSE;
import static elevator.ElevatorState.OPEN;

public class Elevator implements Observer {

    static final Logger LOGGER = Logger.getLogger(Elevator.class.getName());
    private final Queue<Integer> stagesToStop = new PriorityQueue<>();
    private Integer stage = 0;
    private ElevatorState state = ElevatorState.CLOSE;

    public ElevatorState getState() {
        return state;
    }

    public Integer getStage() {
        return stage;
    }

    public Elevator call(Integer atStage, Direction to) {
        LOGGER.entering(this.getClass().getName(), "call", new Object[]{atStage, to});
        if (!stagesToStop.contains(atStage)) {
            stagesToStop.add(atStage);
        }
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
        Integer nextStage = stagesToStop.peek();
        if (nextStage == null) {
            state = CLOSE;
            LOGGER.finer("closing or does nothing");
            return;
        }

        if (state == OPEN) {
            state = CLOSE;
            LOGGER.finer("closing");
            return;
        }

        if (stage < nextStage) {
            stage++;
            LOGGER.finer("up");
            return;
        }

        if (stage > nextStage) {
            stage--;
            LOGGER.finer("down");
            return;
        }

        state = OPEN;
        stagesToStop.poll();
        LOGGER.finer("");
    }

}
