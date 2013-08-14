package elevator.logging;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.LogManager.getLogManager;
import static java.util.logging.Logger.getLogger;

public class ElevatorLogger {

    private final Logger logger;

    public ElevatorLogger(String name) {
        LogManager logManager = getLogManager();
        Logger logger = logManager.getLogger(name);
        if (logger == null) {
            logger = getLogger(name);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            logger.addHandler(new ElevatorConsoleHandler());
            logManager.addLogger(logger);
        }
        this.logger = logger;
    }

    /**
     * Please keep a strong reference on this {@link Logger} as described by
     * {@link java.util.logging.LogManager#getLogger}.
     *
     * @return {@link Logger} instance with configured {@link Level}, {@link java.util.logging.Handler} and
     *         {@link java.util.logging.Formatter}
     */
    public Logger logger() {
        return logger;
    }

}
