package elevator.server.port;

class FixedPort implements Port {

    private final Integer port;

    FixedPort(Integer port) {
        this.port = port;
    }

    @Override
    public Integer port() {
        return port;
    }

    @Override
    public String toString() {
        return port.toString();
    }

}
