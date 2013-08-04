package elevator.server.security;

import static java.lang.Math.random;
import static java.lang.String.format;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class RandomPassword implements Password {

    public static final String password = generate();

    private static String generate() {
        final String chars = "" + //
                "0123456789" + //
                "abcdefghijklmnopqrstuvwxyz" + //
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + //
                "_-";

        final StringBuilder password = new StringBuilder(16);

        for (Integer i = 0; i < 16; i++) {
            password.append(chars.charAt(new Double(random() * chars.length()).intValue()));
        }

        String authToken = printBase64Binary(("admin:" + password.toString()).getBytes());
        String curl = "curl --header 'Authorization: Basic %s' 'http://localhost:8080/resources/admin/%s'";
        System.out.println(format(curl, authToken, "maxNumberOfUsers"));
        System.out.println(format(curl, authToken, "increaseMaxNumberOfUsers"));
        System.out.println(format(curl, authToken, "decreaseMaxNumberOfUsers"));

        return password.toString();
    }

    @Override
    public String value() {
        return password;
    }

}
