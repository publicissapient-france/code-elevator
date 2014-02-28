package elevator.engine.scan.assertions;

import elevator.engine.scan.Command;
import elevator.engine.scan.DistanceEvaluator;

import java.util.LinkedHashSet;
import java.util.Set;

import static elevator.engine.scan.ElevatorDirection.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class DistanceEvaluatorAssert {
    private final DistanceEvaluator actual;

    DistanceEvaluatorAssert(DistanceEvaluator actual) {
        this.actual = actual;
    }

    public void distancesAre(String expectedDistances) {
        final String separator = "+-------+-----------+----------+-----------+----------+";
        final Set<Command> commands = new LinkedHashSet<>();
        commands.add(new Command(0, UP));
        commands.add(new Command(1, UP));
        commands.add(new Command(2, UP));
        commands.add(new Command(3, UP));
        commands.add(new Command(4, UP));
        commands.add(new Command(5, DOWN));
        commands.add(new Command(4, DOWN));
        commands.add(new Command(3, DOWN));
        commands.add(new Command(2, DOWN));
        commands.add(new Command(1, DOWN));

        StringBuilder actualDistances = new StringBuilder();
        actualDistances.append(separator).append('\n');
        actualDistances.append("| floor | direction | distance | direction | distance |\n");
        actualDistances.append(separator).append('\n');
        for (Command command : commands) {
            actualDistances.append(format("| %5d | %9s | %8d |      NONE | %8d |\n",
                    command.floor,
                    command.direction, actual.getDistance(command),
                    actual.getDistance(new Command(command.floor, NONE))));
        }
        actualDistances.append(separator);

        assertThat(actualDistances.toString()).isEqualTo(expectedDistances);
    }
}
