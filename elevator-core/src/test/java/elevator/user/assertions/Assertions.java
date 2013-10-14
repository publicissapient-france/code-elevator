package elevator.user.assertions;

import elevator.user.InitializationStrategy;

public class Assertions {

    public static InitializationStrategyAssert assertThat(InitializationStrategy actual) {
        return new InitializationStrategyAssert(actual);
    }

}
