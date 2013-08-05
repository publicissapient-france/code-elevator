package elevator.server.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

class ElevatorConsoleHandler extends ConsoleHandler {

    ElevatorConsoleHandler() {
        setLevel(Level.ALL);
        setFormatter(new ElevatorFormatter());
    }

    @Override
    protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
        super.setOutputStream(System.out);
    }

}
