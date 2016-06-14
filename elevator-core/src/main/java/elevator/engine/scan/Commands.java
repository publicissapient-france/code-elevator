package elevator.engine.scan;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static elevator.engine.scan.ElevatorDirection.NONE;
import static java.lang.Math.abs;
import static java.util.Collections.unmodifiableSet;

public class Commands {

    private final Set<Command> commands;
    private final Integer lowerFloor;
    private final Integer higherFloor;

    Commands(Integer lowerFloor, Integer higherFloor) {
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

    public Command get(Integer floor, ElevatorDirection elevatorDirection) {
        if (commands.isEmpty()) {
            return null;
        }
        SortedSet<Command> sortedCommands = sortCommands(floor, elevatorDirection);
        return sortedCommands.first();
    }

    Command next(Integer floor) {
        for (Command command : sortCommands(floor, NONE)) {
            if (!command.floor.equals(floor)) {
                return command;
            }
        }
        return null;
    }

    void reset() {
        commands.clear();
    }

    void clear(Integer floor, ElevatorDirection direction) {
        commands.removeIf(command -> command.floor.equals(floor) && (command.direction == NONE || command.direction == direction));
    }

    private SortedSet<Command> sortCommands(final Integer floor, final ElevatorDirection elevatorDirection) {
        SortedSet<Command> sortedCommands = new TreeSet<>((o1, o2) -> {
            DistanceEvaluator distanceEvaluator = new DistanceEvaluator(new Command(floor, elevatorDirection), lowerFloor, higherFloor);
            Integer distance1 = distanceEvaluator.getDistance(o1);
            Integer distance2 = distanceEvaluator.getDistance(o2);
            return distance1 - distance2;
        });
        sortedCommands.addAll(commands);
        return sortedCommands;
    }

}
