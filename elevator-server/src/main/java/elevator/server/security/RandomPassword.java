package elevator.server.security;

import elevator.logging.ElevatorLogger;

import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.lang.String.format;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class RandomPassword implements Password {

    private final Logger logger;
    private final String password;

    public RandomPassword() {
        this.logger = new ElevatorLogger("RandomPassword").logger();
        this.password = generate();
        this.logger.config(password);
        String authToken = printBase64Binary(("admin:" + password).getBytes());
        String curl = "curl --header 'Authorization: Basic %s' 'http://<server>[:<port>]/resources/admin/%s'";
        this.logger.config(format(curl, authToken, "maxNumberOfUsers"));
        this.logger.config(format(curl, authToken, "increaseMaxNumberOfUsers"));
        this.logger.config(format(curl, authToken, "decreaseMaxNumberOfUsers"));

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
