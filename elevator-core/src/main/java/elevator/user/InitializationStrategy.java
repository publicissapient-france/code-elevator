package elevator.user;

@FunctionalInterface
public interface InitializationStrategy {

    FloorsAndDirection create();

}
