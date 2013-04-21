package elevator.server.port;

import static java.lang.Math.random;
import static java.lang.Math.round;

class RandomPort implements Port {

    private final int port;

    RandomPort() {
        this.port = (int) round(random() * 10000) + 1024;
    }

    @Override
    public String toString() {
        return Integer.toString(port);
    }

    @Override
    public Integer port() {
        return port;
    }

}
