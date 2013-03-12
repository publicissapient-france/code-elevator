package elevator;

import java.util.HashSet;
import java.util.Set;

public class Clock {

    private final Set<ClockListener> clockListeners = new HashSet<>();

    public Clock addClockListener(ClockListener clockListener) {
        clockListeners.add(clockListener);
        return this;
    }

    public void tick() {
        for (ClockListener clockListener : clockListeners) {
            clockListener.onTick();
        }
    }

}
