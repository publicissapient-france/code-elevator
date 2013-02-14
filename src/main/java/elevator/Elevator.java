package elevator;

import java.util.*;
import java.util.logging.Logger;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.ElevatorState.CLOSE;
import static elevator.ElevatorState.OPEN;

public class Elevator implements Observer {

    private static final Integer maxStage = 5;

    private static final Logger LOGGER = Logger.getLogger(Elevator.class.getName());
    private final Queue<Order> orders = new PriorityQueue<Order>(11, (o1, o2) -> {
        ElevatorAndNextStageComparator elevatorAndNextStageComparator = new ElevatorAndNextStageComparator();
        Order elevator = new Order(stage, direction);

        int distanceo1 = elevatorAndNextStageComparator.compare(elevator, o1);
        if (distanceo1 > 0) distanceo1 += (maxStage - 1) * 2;
        int distanceo2 = elevatorAndNextStageComparator.compare(elevator, o2);
        if (distanceo2 > 0) distanceo2 += (maxStage - 1) * 2;

        return distanceo1 - distanceo2;
    });
    private Integer stage = 0;
    private Direction direction;
    private ElevatorState state = CLOSE;

    public ElevatorState getState() {
        return state;
    }

    public Integer getStage() {
        return stage;
    }

    public Elevator call(Integer atStage, Direction to) {
        LOGGER.entering(this.getClass().getName(), "call", new Object[]{atStage, to});
        Order newStageToStop = new Order(atStage, to);
        if (!orders.contains(newStageToStop)) {
            orders.add(newStageToStop);
        }
        return this;
    }

    public Elevator go(Integer stageToGo) {
        LOGGER.entering(this.getClass().getName(), "go", stageToGo);
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
        LOGGER.entering(this.getClass().getName(), "update", new Object[]{clock, arg});
        Order nextStage = orders.peek();
        if (nextStage == null) {
            state = CLOSE;
            direction = null;
            LOGGER.finer("closing or does nothing");
            return;
        }

        if (state == OPEN) {
            state = CLOSE;
            direction = null;
            LOGGER.finer("closing");
            return;
        }

        int compare = new ElevatorAndNextStageComparator().compare(new Order(stage, direction), nextStage);

        if (compare < 0) { // stage < nextStage.stage
            stage++;
            direction = UP;
            LOGGER.finer("up");
            return;
        }

        if (compare > 0) { // stage > nextStage.stage
            stage--;
            direction = DOWN;
            LOGGER.finer("down");
            return;
        }

        state = OPEN;
        direction = null;
        orders.poll();
        LOGGER.finer("open");
    }

    @Override
    public String toString() {
        return "elevator " + (direction == null ? state : direction) + " " + stage;
    }

    private class Order {
        final Integer stage;
        final Direction direction;

        Order(Integer stage, Direction direction) {
            this.stage = stage;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            Order that = (Order) o;

            return direction == that.direction && stage.equals(that.stage);
        }

        @Override
        public int hashCode() {
            int result = stage != null ? stage.hashCode() : 0;
            result = 31 * result + (direction != null ? direction.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return stage + " " + direction;
        }
    }

    private class ElevatorAndNextStageComparator implements Comparator<Order> {

        @Override
        public int compare(Order o1, Order o2) {
            if (o1.direction == null || o2.direction == null) {
                return compareStages(o1, o2);
            } else {
                return compareStagesAndDirections(o1, o2);
            }
        }

        private Integer compareStages(Order o1, Order o2) {
            return o1.stage - o2.stage;
        }

        private Integer compareStagesAndDirections(Order o1, Order o2) {
            return comparisonIndexWithDirection(o1) - comparisonIndexWithDirection(o2);
        }

        private Integer comparisonIndexWithDirection(Order order) {
            if (order.direction == UP) {
                return order.stage;
            } else {
                return 2 * (maxStage - 1) - order.stage;
            }
        }
    }
}
