package elevator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Clock {

    public final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private final Set<ClockListener> clockListeners = new HashSet<>();

    public Clock addClockListener(ClockListener clockListener) {
        clockListeners.add(clockListener);
        return this;
    }

    public Clock removeClockListener(ClockListener clockListener) {
        clockListeners.remove(clockListener);
        return this;
    }

    public void tick() {
        for (ClockListener clockListener : clockListeners) {
            EXECUTOR_SERVICE.execute(clockListener::onTick);
        }
    }

}
