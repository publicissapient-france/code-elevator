package elevator.client;

import java.util.Observable;
import java.util.Observer;

import static elevator.client.Direction.DOWN;
import static elevator.client.Direction.UP;

public class Elevator implements Observer {

    private static final Integer maxStage = 5;

    private final Commands commands = new Commands(0, maxStage);

    private Integer stage = 0;
    private Direction direction;
    private ElevatorState state = ElevatorState.CLOSE;

    public ElevatorState state() {
        return state;
    }

    public Integer stage() {
        return stage;
    }

    public Elevator call(Integer atStage, Direction to) {
        commands.add(new Command(atStage, to));
        return this;
    }

    public Elevator go(Integer stageToGo) {
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
        Command nextCommand;
        if (direction == null) {
            nextCommand = commands.get(stage);
        } else {
            nextCommand = commands.get(stage, direction);
        }
        if (nextCommand == null) {
            state = ElevatorState.CLOSE;
            direction = null;
            return;
        }

        if (state == ElevatorState.OPEN) {
            state = ElevatorState.CLOSE;
            direction = null;
            return;
        }

        direction = nextCommand.getDirection(stage);

        if (nextCommand.equals(new Command(stage, direction)) || (nextCommand.stage.equals(stage) && commands.commands().isEmpty())) {
            state = ElevatorState.OPEN;
            direction = null;
            return;
        }

        if (direction == UP) {
            stage++;
            return;
        }

        if (direction == DOWN) {
            stage--;
        }
    }

    @Override
    public String toString() {
        return "elevator " + (direction == null ? state : direction) + " " + stage;
    }

}
