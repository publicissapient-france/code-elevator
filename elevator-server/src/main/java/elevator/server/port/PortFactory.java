package elevator.server.port;

class PortFactory {

    static final String ELEVATOR_SERVER_PORT_PROPERTY = "elevator.server.port";

    static Port newPort() {
        Integer port = Integer.getInteger(ELEVATOR_SERVER_PORT_PROPERTY);
        if (port == null) {
            return new RandomPort();
        }
        return new FixedPort(port);
    }

}
