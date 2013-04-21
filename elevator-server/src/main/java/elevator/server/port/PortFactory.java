package elevator.server.port;

public class PortFactory {

    public static final String ELEVATOR_SERVER_PORT_PROPERTY = "elevator.server.port";

    public static Port newPort() {
        Integer port = Integer.getInteger(ELEVATOR_SERVER_PORT_PROPERTY);
        if (port == null) {
            return new RandomPort();
        }
        return new FixedPort(port);
    }

}
