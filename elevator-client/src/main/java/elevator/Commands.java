package elevator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static java.lang.Math.abs;
import static java.util.Collections.unmodifiableSet;

public class Commands {

    private final Set<Command> commands;
    private final Integer minStage;
    private final Integer maxStage;

    public Commands(Integer minStage, Integer maxStage) {
        this.minStage = minStage;
        this.maxStage = maxStage;
        commands = new LinkedHashSet<>(abs(maxStage - minStage) * 2);
    }

    Set<Command> commands() {
        return unmodifiableSet(commands);
    }

    public Commands add(Command command) {
        commands.add(command);
        return this;
    }

    public Command get(Integer stage) {
        if (commands.isEmpty()) {
            return null;
        }
        return get(stage, getDirection(stage));
    }

    public Command get(Integer stage, Direction direction) {
        if (direction == null) {
            throw new NullPointerException();
        }
        if (commands.isEmpty()) {
            return null;
        }
        final Command commandFromElevator = new Command(stage, direction);
        if (commands.contains(commandFromElevator)) {
            commands.remove(commandFromElevator);
            return commandFromElevator;
        }
        if (commands.size() == 1) {
            return commands.iterator().next();
        }
        SortedSet<Command> sortedCommands = new TreeSet<Command>((o1, o2) -> {
            DistanceEvaluator distanceEvaluator = new DistanceEvaluator(commandFromElevator, minStage, maxStage);
            Integer distance1 = distanceEvaluator.getDistance(o1);
            Integer distance2 = distanceEvaluator.getDistance(o2);
            return distance1 - distance2;
        });
        sortedCommands.addAll(commands);
        return sortedCommands.first();
    }

    private Direction getDirection(Integer stage) {
        return commands.iterator().next().getDirection(stage);
    }

}
