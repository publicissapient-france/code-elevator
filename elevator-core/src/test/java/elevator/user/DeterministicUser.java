package elevator.user;

public class DeterministicUser implements InitializationStrategy {

    private final FloorsAndDirection floorsAndDirection;

    public DeterministicUser(Integer initialFloor, Integer floorToGo) {
        floorsAndDirection = new FloorsAndDirection(initialFloor, floorToGo);
    }

    @Override
    public FloorsAndDirection create() {
        return floorsAndDirection;
    }

}
