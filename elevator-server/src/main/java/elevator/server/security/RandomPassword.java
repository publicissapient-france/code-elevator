package elevator.server.security;

import elevator.logging.ElevatorLogger;

import java.util.logging.Logger;

import static java.lang.Math.random;

public class RandomPassword implements Password {

    private final Logger logger;
    private final String password;

    public RandomPassword() {
        this.logger = new ElevatorLogger("RandomPassword").logger();
        this.password = generate();
        this.logger.config(password);
    }

    private String generate() {
        final String chars = "" +
                "0123456789" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "_-";

        final StringBuilder password = new StringBuilder(16);

        for (Integer i = 0; i < 16; i++) {
            password.append(chars.charAt(new Double(random() * chars.length()).intValue()));
        }

        return password.toString();
    }

    @Override
    public String value() {
        return password;
    }

}
