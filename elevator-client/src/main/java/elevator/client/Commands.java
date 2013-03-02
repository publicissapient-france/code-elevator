package elevator.client;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.lang.Math.abs;
import static java.util.Collections.unmodifiableSet;

public class Commands {

    private final Set<Command> commands;
    private final Integer minFloor;
    private final Integer maxFloor;

    public Commands(Integer minFloor, Integer maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        commands = new LinkedHashSet<>(abs(maxFloor - minFloor) * 2);
    }

    Set<Command> commands() {
        return unmodifiableSet(commands);
    }

    public Commands add(Command command) {
        commands.add(command);
        return this;
    }

    public Command get(Integer floor) {
        if (commands.isEmpty()) {
            return null;
        }
        return get(floor, getDirection(floor));
    }

    public Command get(Integer floor, Direction direction) {
        if (direction == null) {
            throw new NullPointerException();
        }
        if (commands.isEmpty()) {
            return null;
        }
        final Command commandFromElevator = new Command(floor, direction);
        if (commands.contains(commandFromElevator)) {
            commands.remove(commandFromElevator);
            return commandFromElevator;
        }
        if (commands.size() == 1) {
            return commands.iterator().next();
        }
        SortedSet<Command> sortedCommands = new TreeSet<Command>((o1, o2) -> {
            DistanceEvaluator distanceEvaluator = new DistanceEvaluator(commandFromElevator, minFloor, maxFloor);
            Integer distance1 = distanceEvaluator.getDistance(o1);
            Integer distance2 = distanceEvaluator.getDistance(o2);
            return distance1 - distance2;
        });
        sortedCommands.addAll(commands);
        return sortedCommands.first();
    }

    private Direction getDirection(Integer floor) {
        return commands.iterator().next().getDirection(floor);
    }

}
