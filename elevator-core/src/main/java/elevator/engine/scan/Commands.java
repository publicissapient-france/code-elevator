package elevator.engine.scan;

import elevator.Direction;

import java.util.*;

import static java.lang.Math.abs;
import static java.util.Collections.unmodifiableSet;

public class Commands {

    private final Set<Command> commands;
    private final Integer lowerFloor;
    private final Integer higherFloor;

    public Commands(Integer lowerFloor, Integer higherFloor) {
        this.lowerFloor = lowerFloor;
        this.higherFloor = higherFloor;
        commands = new LinkedHashSet<>(abs(higherFloor - lowerFloor) * 2);
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
        Direction direction = getDirection(floor);
        final Command commandFromElevator = new Command(floor, direction);
        if (commands.contains(commandFromElevator)) {
            commands.remove(commandFromElevator);
            return commandFromElevator;
        }
        if (commands.size() == 1) {
            return commands.iterator().next();
        }
        SortedSet<Command> sortedCommands = new TreeSet<>(new Comparator<Command>() {
            @Override
            public int compare(Command o1, Command o2) {
                DistanceEvaluator distanceEvaluator = new DistanceEvaluator(commandFromElevator, lowerFloor, higherFloor);
                Integer distance1 = distanceEvaluator.getDistance(o1);
                Integer distance2 = distanceEvaluator.getDistance(o2);
                return distance1 - distance2;
            }
        });
        sortedCommands.addAll(commands);
        return sortedCommands.first();
    }

    private Direction getDirection(Integer floor) {
        return commands.iterator().next().getDirection(floor);
    }

}
