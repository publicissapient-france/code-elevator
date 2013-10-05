package elevator.engine.scan.assertions;

import elevator.engine.scan.Command;
import elevator.engine.scan.Commands;
import elevator.engine.scan.DistanceEvaluator;

public class Assertions {

    public static CommandAssert assertThat(Command actual) {
        return new CommandAssert(actual);
    }

    public static CommandsAssert assertThat(Commands actual) {
        return new CommandsAssert(actual);
    }

    public static DistanceEvaluatorAssert assertThat(DistanceEvaluator actual) {
        return new DistanceEvaluatorAssert(actual);
    }

}
