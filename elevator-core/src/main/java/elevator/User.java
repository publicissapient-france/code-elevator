package elevator;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static java.lang.Math.random;

public class User {

    public final Integer floor;
    public final Direction direction;

    public User() {
        if (random() > .5) {
            this.floor = new Double(random() * 5).intValue();
            this.direction = random() > .5 ? UP : DOWN;
        } else {
            this.floor = 0;
            this.direction = UP;
        }
    }

}
